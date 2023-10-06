package com.amrtm.microservice.store.production.controller.configuration.listeners

import com.amrtm.microservice.store.transformationandevaluationproduct.controller.service.kafka.KafkaService
import com.amrtm.microservice.store.production.model.Product
import com.amrtm.microservice.store.production.model.kafka.KafkaMessage
import jakarta.persistence.PostPersist
import jakarta.persistence.PostRemove
import jakarta.persistence.PostUpdate
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.stereotype.Service

@Service
class GroupEntityListener(
    var kafkaService: KafkaService,
    var globalAppsTopic: NewTopic
): ProductEntityListener<Product> {
    @PostPersist
    override fun loadPost(entity: Product) {
        kafkaService.sendLog(globalAppsTopic,"Group_Stored", KafkaMessage(2L,"stored:${entity}"))
    }
    @PostUpdate
    override fun updatePost(entity: Product) {
        kafkaService.sendLog(globalAppsTopic,"Group_Updated", KafkaMessage(2L,"updated:${entity}"))
    }
    @PostRemove
    override fun deletePost(entity: Product) {
        kafkaService.sendLog(globalAppsTopic,"Group_Deleted", KafkaMessage(2L,"deleted:${entity}"))
    }
}