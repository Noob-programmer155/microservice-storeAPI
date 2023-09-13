package com.amrtm.microservice.store.production.controller.service

import com.amrtm.microservice.store.production.controller.repository.GroupRepository
import com.amrtm.microservice.store.production.controller.service.kafka.KafkaService
import com.amrtm.microservice.store.production.model.Group
import com.amrtm.microservice.store.production.model.kafka.KafkaMessage
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.stereotype.Service
import java.util.*

@Service
class GroupService(var groupRepository: GroupRepository, var kafkaService: KafkaService, var errorAppsTopic: NewTopic,
                   var globalAppsTopic: NewTopic) {
    // ElasticSearch
//    get
//    getAll
//    getAll
    // MySQL
    fun store(data: Group) {
        try {
            groupRepository.save(data)
        } catch (err: Exception) {
            kafkaService.sendLog(errorAppsTopic,"error_group_store", KafkaMessage(1L,err.localizedMessage),
                    globalAppsTopic) { msg ->
                KafkaMessage(1L, msg.message?.split("[A-Z]{9}".toRegex())
                        ?.joinToString(prefix = "error:", postfix = "..."))
            }
        }
    }
    fun delete(id: UUID) {
        try {
            groupRepository.deleteById(id)
        } catch (err: Exception) {
            kafkaService.sendLog(errorAppsTopic,"error_group_delete", KafkaMessage(1L,err.localizedMessage),
                    globalAppsTopic) { msg ->
                KafkaMessage(1L, msg.message?.split("[A-Z]{9}".toRegex())
                        ?.joinToString(prefix = "error:", postfix = "..."))
            }
        }
    }
}
