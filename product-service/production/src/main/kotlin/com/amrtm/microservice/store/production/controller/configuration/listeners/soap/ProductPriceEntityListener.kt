package com.amrtm.microservice.store.production.controller.configuration.listeners.soap

import com.amrtm.microservice.store.production.controller.configuration.listeners.ProductEntityListener
import com.amrtm.microservice.store.production.controller.service.kafka.KafkaService
import com.amrtm.microservice.store.production.model.kafka.KafkaMessage
import com.amrtm.microservice.store.production.model.soap.SOAPResponseGlobal
import com.amrtm.microservice.store.production.model.store_only.ProductPrice
import jakarta.persistence.PostPersist
import jakarta.persistence.PostRemove
import jakarta.persistence.PostUpdate
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.stereotype.Service
import org.springframework.ws.client.core.WebServiceTemplate

@Service
class ProductPriceEntityListener(
    var kafkaService: KafkaService,
    var webServiceTemplate: WebServiceTemplate,
    var globalAppsTopic: NewTopic
): ProductEntityListener<ProductPrice> {
    @PostPersist
    override fun loadPost(entity: ProductPrice) {
        kafkaService.sendLog(globalAppsTopic,"Product_Price_Stored", KafkaMessage(2L,"stored:${entity}"))
            ?.thenApply {
                val url = "${webServiceTemplate.defaultUri}/product/price"
                val responseObject = webServiceTemplate.marshalSendAndReceive(url,entity) as SOAPResponseGlobal
                kafkaService.sendLog(globalAppsTopic,"send_to_evaluation_service",
                    KafkaMessage(3L,"${
                        if(responseObject.err) "successfully sent on Product Price:store:"
                        else "error on sent Product Price:store:"} ${responseObject.message}"))
            }
    }
    @PostUpdate
    override fun updatePost(entity: ProductPrice) {
        kafkaService.sendLog(globalAppsTopic,"Product_Price_Updated", KafkaMessage(2L,"updated:${entity}"))
            ?.thenApply {
                val url = "${webServiceTemplate.defaultUri}/product/price/update"
                val responseObject = webServiceTemplate.marshalSendAndReceive(url,entity) as SOAPResponseGlobal
                kafkaService.sendLog(globalAppsTopic,"send_to_evaluation_service",
                    KafkaMessage(3L,"${
                        if(responseObject.err) "successfully sent on Product Price:update:"
                        else "error on sent Product Price:update:"} ${responseObject.message}"))
            }
    }
    @PostRemove
    override fun deletePost(entity: ProductPrice) {
        kafkaService.sendLog(globalAppsTopic,"Product_Price_Deleted", KafkaMessage(2L,"deleted:${entity}"))
            ?.thenApply {
                val url = "${webServiceTemplate.defaultUri}/product/price/delete"
                val responseObject = webServiceTemplate.marshalSendAndReceive(url,entity) as SOAPResponseGlobal
                kafkaService.sendLog(globalAppsTopic,"send_to_evaluation_service",
                    KafkaMessage(3L,"${
                        if(responseObject.err) "successfully sent on on Product Price:delete:"
                        else "error on sent Product Price:delete:"} ${responseObject.message}"))
            }
    }
}