package com.amrtm.microservice.store.production.controller.configuration.listeners.soap

import com.amrtm.microservice.store.production.controller.configuration.listeners.ProductEntityListener
import com.amrtm.microservice.store.production.controller.service.kafka.KafkaService
import com.amrtm.microservice.store.production.model.kafka.KafkaMessage
import com.amrtm.microservice.store.production.model.soap.SOAPResponseGlobal
import com.amrtm.microservice.store.production.model.store_only.ProductStock
import jakarta.persistence.PostPersist
import jakarta.persistence.PostRemove
import jakarta.persistence.PostUpdate
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.stereotype.Service
import org.springframework.ws.client.core.WebServiceTemplate

@Service
class ProductStockEntityListener(
    var kafkaService: KafkaService,
    var webServiceTemplate: WebServiceTemplate,
    var globalAppsTopic: NewTopic
): ProductEntityListener<ProductStock> {
    @PostPersist
    override fun loadPost(entity: ProductStock) {
        kafkaService.sendLog(globalAppsTopic,"Product_Stock_Stored", KafkaMessage(2L,"stored:${entity}"))
            ?.thenApply {
                val url = "${webServiceTemplate.defaultUri}/product/stock"
                val responseObject = webServiceTemplate.marshalSendAndReceive(url,entity) as SOAPResponseGlobal
                kafkaService.sendLog(globalAppsTopic,"send_to_evaluation_service",
                    KafkaMessage(3L,"${
                        if(responseObject.err) "successfully sent on Product Stock:store:"
                        else "error on sent Product Stock:store:"} ${responseObject.message}"))
            }
    }
    @PostUpdate
    override fun updatePost(entity: ProductStock) {
        kafkaService.sendLog(globalAppsTopic,"Product_Stock_Updated", KafkaMessage(2L,"updated:${entity}"))
            ?.thenApply {
                val url = "${webServiceTemplate.defaultUri}/product/stock/update"
                val responseObject = webServiceTemplate.marshalSendAndReceive(url,entity) as SOAPResponseGlobal
                kafkaService.sendLog(globalAppsTopic,"send_to_evaluation_service",
                    KafkaMessage(3L,"${
                        if(responseObject.err) "successfully sent on Product Stock:update:"
                        else "error on sent Product Stock:update:"} ${responseObject.message}"))
            }
    }
    @PostRemove
    override fun deletePost(entity: ProductStock) {
        kafkaService.sendLog(globalAppsTopic,"Product_Stock_Deleted", KafkaMessage(2L,"deleted:${entity}"))
            ?.thenApply {
                val url = "${webServiceTemplate.defaultUri}/product/stock/delete"
                val responseObject = webServiceTemplate.marshalSendAndReceive(url,entity) as SOAPResponseGlobal
                kafkaService.sendLog(globalAppsTopic,"send_to_evaluation_service",
                    KafkaMessage(3L,"${
                        if(responseObject.err) "successfully sent on on Product Stock:delete:"
                        else "error on sent Product Stock:delete:"} ${responseObject.message}"))
            }
    }
}