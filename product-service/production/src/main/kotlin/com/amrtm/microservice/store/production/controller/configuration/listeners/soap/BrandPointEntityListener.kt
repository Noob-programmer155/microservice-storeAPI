package com.amrtm.microservice.store.production.controller.configuration.listeners.soap

import com.amrtm.microservice.store.production.controller.configuration.listeners.ProductEntityListener
import com.amrtm.microservice.store.production.controller.service.kafka.KafkaService
import com.amrtm.microservice.store.production.model.kafka.KafkaMessage
import com.amrtm.microservice.store.production.model.soap.SOAPResponseGlobal
import com.amrtm.microservice.store.production.model.store_only.BrandPoint
import jakarta.persistence.PostPersist
import jakarta.persistence.PostRemove
import jakarta.persistence.PostUpdate
import jakarta.persistence.PrePersist
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.stereotype.Service
import org.springframework.ws.client.core.WebServiceTemplate

@Service
class BrandPointEntityListener(
    var kafkaService: KafkaService,
    var webServiceTemplate: WebServiceTemplate,
    var globalAppsTopic: NewTopic
): ProductEntityListener<BrandPoint> {
    @PostPersist
    override fun loadPost(entity: BrandPoint) {
        kafkaService.sendLog(globalAppsTopic,"Brand_Point_Stored", KafkaMessage(2L,"stored:${entity}"))
            ?.thenApply {
                val url = "${webServiceTemplate.defaultUri}/brand/point"
                val responseObject = webServiceTemplate.marshalSendAndReceive(url,entity) as SOAPResponseGlobal
                kafkaService.sendLog(globalAppsTopic,"send_to_evaluation_service",
                    KafkaMessage(3L,"${
                        if(responseObject.err) "successfully sent on Brand Point:store:"
                        else "error on sent Brand Point:store:"} ${responseObject.message}"))
            }
    }
    @PostUpdate
    override fun updatePost(entity: BrandPoint) {
        kafkaService.sendLog(globalAppsTopic,"Brand_Point_Updated", KafkaMessage(2L,"updated:${entity}"))
            ?.thenApply {
                val url = "${webServiceTemplate.defaultUri}/brand/point"
                val responseObject = webServiceTemplate.marshalSendAndReceive(url,entity) as SOAPResponseGlobal
                kafkaService.sendLog(globalAppsTopic,"send_to_evaluation_service",
                    KafkaMessage(3L,"${
                        if(responseObject.err) "successfully sent on Brand Point:update:"
                        else "error on sent Brand Point:update:"} ${responseObject.message}"))
            }
    }
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
}