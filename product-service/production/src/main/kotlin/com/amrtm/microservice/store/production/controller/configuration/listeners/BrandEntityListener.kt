package com.amrtm.microservice.store.production.controller.configuration.listeners

import com.amrtm.microservice.store.production.controller.service.kafka.KafkaService
import com.amrtm.microservice.store.production.model.Brand
import com.amrtm.microservice.store.production.model.kafka.KafkaMessage
import jakarta.persistence.PostPersist
import jakarta.persistence.PostRemove
import jakarta.persistence.PostUpdate
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.stereotype.Service

@Service
class BrandEntityListener(
    var kafkaService: KafkaService,
    var globalAppsTopic: NewTopic
): ProductEntityListener<Brand> {
    @PostPersist
    override fun loadPost(entity: Brand) {
        kafkaService.sendLog(globalAppsTopic,"Brand_Stored", KafkaMessage(2L,"stored:${entity}"))
    }
    @PostUpdate
    override fun updatePost(entity: Brand) {
        kafkaService.sendLog(globalAppsTopic,"Brand_Updated", KafkaMessage(2L,"updated:${entity}"))
    }
    @PostRemove
    override fun deletePost(entity: Brand) {
        kafkaService.sendLog(globalAppsTopic,"Brand_Deleted", KafkaMessage(2L,"deleted:${entity}"))
    }
}