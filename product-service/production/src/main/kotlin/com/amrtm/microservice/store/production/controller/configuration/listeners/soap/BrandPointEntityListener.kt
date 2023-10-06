package com.amrtm.microservice.store.production.controller.configuration.listeners.soap

import com.amrtm.microservice.store.production.controller.configuration.listeners.ProductEntityListener
import com.amrtm.microservice.store.production.controller.configuration.listeners.soap.transaction.EntityListenerBrandPointSave
import com.amrtm.microservice.store.production.controller.configuration.listeners.soap.transaction.EntityListenerBrandPointUpdate
import com.amrtm.microservice.store.production.controller.repository.store_only.BrandPointRepository
import com.amrtm.microservice.store.production.controller.service.kafka.KafkaService
import com.amrtm.microservice.store.production.model.kafka.KafkaMessage
import com.amrtm.microservice.store.production.model.soap.SOAPResponseGlobal
import com.amrtm.microservice.store.production.model.store_only.BrandPoint
import jakarta.persistence.*
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Lazy
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.ws.client.core.WebServiceTemplate


@Service
class BrandPointEntityListener(
    var kafkaService: KafkaService,
    @Lazy var brandPointRepository: BrandPointRepository,
    var webServiceTemplate: WebServiceTemplate,
    var globalAppsTopic: NewTopic
): ProductEntityListener<BrandPoint> {
    @PostRemove
    override fun deletePost(entity: BrandPoint) {
        kafkaService.sendLog(globalAppsTopic,"Brand_Point_Deleted", KafkaMessage(2L,"deleted:${entity}"))
            ?.thenApply {
                val url = "${webServiceTemplate.defaultUri}/brand/point/delete"
                val responseObject = webServiceTemplate.marshalSendAndReceive(url,entity) as SOAPResponseGlobal
                kafkaService.sendLog(globalAppsTopic,"send_to_evaluation_service",
                    KafkaMessage(3L,"${
                        if(responseObject.err) "successfully sent on on Brand Point:delete:"
                        else "error on sent Brand Point:delete:"} ${responseObject.message}"))
            }
    }
//    @TransactionalEventListener
//    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @EventListener
    fun loadPost(entityBind: EntityListenerBrandPointSave) {
        val entity = entityBind.brandPoint
        entity.score = brandPointRepository.findAvg(entity.brand_id!!).toFloat()
        kafkaService.sendLog(globalAppsTopic, "Brand_Point_Stored", KafkaMessage(2L, "stored:${entity}"))
            ?.thenApply {
                val url = "${webServiceTemplate.defaultUri}/brand/point"
                val responseObject = webServiceTemplate.marshalSendAndReceive(url, entity) as SOAPResponseGlobal
                kafkaService.sendLog(
                    globalAppsTopic, "send_to_evaluation_service",
                    KafkaMessage(
                        3L, "${
                            if (responseObject.err) "successfully sent on Brand Point:store:"
                            else "error on sent Brand Point:store:"
                        } ${responseObject.message}"
                    )
                )
            }
    }
//    @TransactionalEventListener
//    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @EventListener
    fun updatePost(entityBind: EntityListenerBrandPointUpdate) {
        val entity = entityBind.brandPoint
        entity.score = brandPointRepository.findAvg(entity.brand_id!!).toFloat()
        kafkaService.sendLog(globalAppsTopic, "Brand_Point_Updated", KafkaMessage(2L, "updated:${entity}"))
            ?.thenApply {
                val url = "${webServiceTemplate.defaultUri}/brand/point/update"
                val responseObject = webServiceTemplate.marshalSendAndReceive(url, entity) as SOAPResponseGlobal
                kafkaService.sendLog(
                    globalAppsTopic, "send_to_evaluation_service",
                    KafkaMessage(
                        3L, "${
                            if (responseObject.err) "successfully sent on Brand Point:update:"
                            else "error on sent Brand Point:update:"
                        } ${responseObject.message}"
                    )
                )
            }
    }

    override fun loadPost(entity: BrandPoint) {
        TODO("Not yet implemented")
    }

    override fun updatePost(entity: BrandPoint) {
        TODO("Not yet implemented")
    }
}