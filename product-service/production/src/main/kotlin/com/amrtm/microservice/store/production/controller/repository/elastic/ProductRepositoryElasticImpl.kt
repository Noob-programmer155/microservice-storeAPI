package com.amrtm.microservice.store.production.controller.repository.elastic

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient
import co.elastic.clients.elasticsearch._types.SortMode
import co.elastic.clients.elasticsearch._types.query_dsl.Query
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType
import co.elastic.clients.elasticsearch.core.GetRequest
import co.elastic.clients.elasticsearch.core.GetResponse
import co.elastic.clients.elasticsearch.core.SearchRequest
import co.elastic.clients.elasticsearch.core.SearchResponse
import co.elastic.clients.elasticsearch.core.search.Suggestion
import co.elastic.clients.json.JsonData
import com.amrtm.microservice.store.production.model.elastic.ProductElastic
import com.amrtm.microservice.store.production.model.filter.FilterProduct
import com.amrtm.microservice.store.production.model.filter.SearchProduct
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import java.util.UUID
import java.util.concurrent.CompletableFuture

@Component
class ProductRepositoryElasticImpl(
    val elasticsearchClientv2: ElasticsearchAsyncClient
): ProductRepositoryElastic {
    @Value("\${ELASTIC_INDEX_NAME}")
    lateinit var index: String
    override fun getProduct(id: UUID): CompletableFuture<GetResponse<ProductElastic>> {
        return elasticsearchClientv2.get(
            GetRequest.of{
                it.index(index).id(id.toString())
            }, ProductElastic::class.java)
    }
    override fun getProductSearchSuggestion(search: String, page: Pageable): CompletableFuture<Map<String, List<Suggestion<ProductElastic>>>> {
        return elasticsearchClientv2.search(
            SearchRequest.of{
                it.index(index)
                    .sort {s ->
                        s.field {f ->
                            f.field("name.key").mode(SortMode.Min)
                        }
                    }
                    .from(page.offset.toInt())
                    .size(page.pageSize)
                    .query {q ->
                        q.multiMatch {m ->
                            m.query(search)
                                .type(TextQueryType.BoolPrefix)
                                .fields(listOf("name", "name._2gram", "name._3gram"))
                        }
                    }
            }, ProductElastic::class.java).thenApplyAsync { it.suggest() }
    }
    override fun getProductSearch(search: SearchProduct, page: Pageable): CompletableFuture<SearchResponse<ProductElastic>> {
        return elasticsearchClientv2.search(
            SearchRequest.of {
                it.index(index)
                    .sort {s ->
                        s.field {f ->
                            f.field("name.key").mode(SortMode.Min)
                        }
                    }
                    .from(page.offset.toInt())
                    .size(page.pageSize)
                    .query {q ->
                        q.bool {b ->
                            b.should(
                                Query.of{qu -> qu.match {m -> m.field("name").query(search.name)}},
                                Query.of{qu -> qu.match {m -> m.field("brand.name").query(search.brand)}},
                                Query.of{qu -> qu.match {m -> m.field("group.name").query(search.group)}}
                            ).minimumShouldMatch("1").boost(1.0f)
                    }
                }
            }, ProductElastic::class.java
        )
    }
    override fun getProductFilter(filter: FilterProduct, page: Pageable):
            CompletableFuture<SearchResponse<ProductElastic>> {
        return elasticsearchClientv2.search(
            SearchRequest.of {
                it.index(index)
                    .sort {s ->
                        s.field {f ->
                            f.field("name.key").mode(SortMode.Min)
                        }
                    }
                    .from(page.offset.toInt())
                    .size(page.pageSize)
                    .query {q ->
                        q.bool {b ->
                            b.filter(
                                Query.of{qu -> qu.range {r -> r.field("price")
                                    .gte(JsonData.of(filter.priceStart))
                                    .lte(JsonData.of(filter.priceEnd))}},
                                Query.of{qu -> qu.range {r -> r.field("score")
                                    .gte(JsonData.of(filter.scoreStart))
                                    .lte(JsonData.of(filter.scoreEnd))}},
                                Query.of{qu -> qu.range {r -> r.field("brand.score")
                                    .gte(JsonData.of(filter.brandScoreStart))
                                    .lte(JsonData.of(filter.brandScoreEnd))}},
                                Query.of{qu -> qu.match {m -> m.field("keywords")
                                    .query {fv -> fv.anyValue(JsonData.of(filter.keywords))}}},
                                Query.of{qu -> qu.term {t -> t.field("group.type").value(filter.groupType) }},
                            )
                        }
                    }
            }, ProductElastic::class.java
        )
    }
}