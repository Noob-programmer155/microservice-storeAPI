package com.amrtm.microservice.store.production.controller.configuration.listeners

import com.amrtm.microservice.store.production.controller.service.kafka.KafkaService
import com.amrtm.microservice.store.production.model.Group
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
): ProductEntityListener<Group> {
    @PostPersist
    override fun loadPost(entity: Group) {
        kafkaService.sendLog(globalAppsTopic,"Group_Stored", KafkaMessage(2L,"stored:${entity}"))
    }
    @PostUpdate
    override fun updatePost(entity: Group) {
        kafkaService.sendLog(globalAppsTopic,"Group_Updated", KafkaMessage(2L,"updated:${entity}"))
    }
    @PostRemove
    override fun deletePost(entity: Group) {
        kafkaService.sendLog(globalAppsTopic,"Group_Deleted", KafkaMessage(2L,"deleted:${entity}"))
    }
}