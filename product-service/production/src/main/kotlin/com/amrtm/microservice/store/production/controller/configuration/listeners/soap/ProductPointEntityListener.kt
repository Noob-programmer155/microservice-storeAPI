package com.amrtm.microservice.store.production.controller.configuration.listeners.soap

import com.amrtm.microservice.store.production.controller.configuration.listeners.ProductEntityListener
import com.amrtm.microservice.store.production.controller.configuration.listeners.soap.transaction.EntityListenerProductPointSave
import com.amrtm.microservice.store.production.controller.configuration.listeners.soap.transaction.EntityListenerProductPointUpdate
import com.amrtm.microservice.store.production.controller.repository.store_only.ProductPointRepository
import com.amrtm.microservice.store.production.controller.service.kafka.KafkaService
import com.amrtm.microservice.store.production.model.kafka.KafkaMessage
import com.amrtm.microservice.store.production.model.soap.SOAPResponseGlobal
import com.amrtm.microservice.store.production.model.store_only.ProductPoint
import jakarta.persistence.*
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Lazy
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.ws.client.core.WebServiceTemplate


@Service
class ProductPointEntityListener(
    var kafkaService: KafkaService,
    var webServiceTemplate: WebServiceTemplate,
    @Lazy var productPointRepository: ProductPointRepository,
    var globalAppsTopic: NewTopic,
): ProductEntityListener<ProductPoint> {
    @PostRemove
    override fun deletePost(entity: ProductPoint) {
        kafkaService.sendLog(globalAppsTopic,"Product_Point_Deleted", KafkaMessage(2L,"deleted:${entity}"))
            ?.thenApply {
                val url = "${webServiceTemplate.defaultUri}/product/point/delete"
                val responseObject = webServiceTemplate.marshalSendAndReceive(url,entity) as SOAPResponseGlobal
                kafkaService.sendLog(globalAppsTopic,"send_to_evaluation_service",
                    KafkaMessage(3L,"${
                        if(responseObject.err) "successfully sent on on Product Point:delete:"
                        else "error on sent Product Point:delete:"} ${responseObject.message}"))
            }
    }
//    @TransactionalEventListener
//    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @EventListener
    fun loadPost(entityBind: EntityListenerProductPointSave) {
        val entity = entityBind.productPoint
        entity.score = productPointRepository.findAvg(entity.product_id!!).toFloat()
        kafkaService.sendLog(globalAppsTopic,"Product_Point_Stored", KafkaMessage(2L,"stored:${entity}"))
            ?.thenApply {
                val url = "${webServiceTemplate.defaultUri}/product/point"
                val responseObject = webServiceTemplate.marshalSendAndReceive(url,entity) as SOAPResponseGlobal
                kafkaService.sendLog(globalAppsTopic,"send_to_evaluation_service",
                    KafkaMessage(3L,"${
                        if(responseObject.err) "successfully sent on Product Point:store:"
                        else "error on sent Product Point:store:"} ${responseObject.message}"))
            }
    }
//    @TransactionalEventListener
//    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @EventListener
    fun updatePost(entityBind: EntityListenerProductPointUpdate) {
        val entity = entityBind.productPoint
        entity.score = productPointRepository.findAvg(entity.product_id!!).toFloat()
        kafkaService.sendLog(globalAppsTopic,"Product_Point_Updated", KafkaMessage(2L,"updated:${entity}"))
            ?.thenApply {
                val url = "${webServiceTemplate.defaultUri}/product/point/update"
                val responseObject = webServiceTemplate.marshalSendAndReceive(url,entity) as SOAPResponseGlobal
                kafkaService.sendLog(globalAppsTopic,"send_to_evaluation_service",
                    KafkaMessage(3L,"${
                        if(responseObject.err) "successfully sent on Product Point:update:"
                        else "error on sent Product Point:update:"} ${responseObject.message}"))
            }
    }
    override fun loadPost(entity: ProductPoint) {
        TODO("Not yet implemented")
    }

    override fun updatePost(entity: ProductPoint) {
        TODO("Not yet implemented")
    }
}