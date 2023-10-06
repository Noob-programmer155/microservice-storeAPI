package com.amrtm.microservice.store.transformationandevaluationproduct.controller.repository

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient
import co.elastic.clients.elasticsearch._types.Refresh
import co.elastic.clients.elasticsearch._types.mapping.Property
import co.elastic.clients.elasticsearch.core.IndexResponse
import co.elastic.clients.elasticsearch.core.UpdateRequest
import com.amrtm.microservice.store.transformationandevaluationproduct.controller.service.kafka.KafkaService
import com.amrtm.microservice.store.transformationandevaluationproduct.model.elastic.ProductElastic
import com.amrtm.microservice.store.transformationandevaluationproduct.model.kafka.KafkaMessage
import jakarta.annotation.PostConstruct
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class ProductRepositoryElasticImpl(
    val elasticsearchClientv2: ElasticsearchAsyncClient,
    val kafkaService: KafkaService,
    val globalAppsTopic: NewTopic
): ProductRepositoryElastic<ProductElastic,UUID> {
    @Value("\${ELASTIC_INDEX_NAME}")
    lateinit var index:String
    override fun save(product:ProductElastic, id: UUID) {
        elasticsearchClientv2.exists { it.index(index).id(id.toString()) }
            .thenAcceptAsync {
                if (!it.value())
                    elasticsearchClientv2.index {
                        it.index(index)
                            .id(id.toString())
                            .document(product)
                            .refresh(Refresh.True)
                    }.thenAcceptAsync {
                        kafkaService.sendLog(globalAppsTopic,"save_product_elastic",
                            KafkaMessage(2L,"product elastic saved: ${id}"))
                    }
                else
                    elasticsearchClientv2.update(
                        UpdateRequest.of<ProductElastic,Any>{
                            it.index(index)
                                .id(id.toString())
                                .upsert(product)
                        },ProductElastic::class.java
                    ).thenAcceptAsync{
                        kafkaService.sendLog(globalAppsTopic,"update_product_elastic",
                            KafkaMessage(2L,"product elastic updated: ${id}"))
                    }
            }
    }

    override fun delete(id:UUID) {
        elasticsearchClientv2.exists { it.index(index).id(id.toString()) }
            .thenAcceptAsync {
                if (it.value())
                    elasticsearchClientv2.delete {
                        it.index(index).id(id.toString())
                    }
            }
    }
    @PostConstruct
    protected fun initCreate() {
        elasticsearchClientv2.indices().exists { it.index(index) }
            .thenApplyAsync {
                if (!it.value()) {
                    elasticsearchClientv2.indices().create {
                        it.index(index)
                            .mappings{
                                it.properties("name", Property.of{it.searchAsYouType { it.fields(
                                    mutableMapOf(
                                        Pair("key",Property.of { it.keyword { it } })
                                    )
                                )}})
                                    .properties("brand",Property.of { it.`object` {
                                        it.properties("name",Property.of { it.searchAsYouType { it }})
                                            .properties("score",Property.of { it.integer { it } })
                                    }})
                                    .properties("keywords",Property.of { it.keyword { it } })
                                    .properties("score",Property.of { it.float_ { it } })
                                    .properties("stock",Property.of { it.long_ { it } })
                                    .properties("price",Property.of { it.long_ { it } })
                                    .properties("description",Property.of { it.text { it.fields(
                                        mutableMapOf(
                                            Pair("hint",Property.of { it.wildcard { it } })
                                        )
                                    )}})
                                    .properties("group",Property.of { it.`object` {
                                        it.properties("name",Property.of { it.searchAsYouType { it }})
                                            .properties("type",Property.of { it.text { it.fields(
                                                mutableMapOf(
                                                    Pair("key",Property.of { it.keyword { it } })
                                                )
                                            )}})
                                            .properties("subgroups",Property.of { it.nested { it.includeInParent(true) }})
                                    }})
                                    .properties("disable",Property.of { it.boolean_{ it } })
                            }
                    }
                }
            }
    }
}