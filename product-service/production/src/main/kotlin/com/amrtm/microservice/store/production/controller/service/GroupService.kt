package com.amrtm.microservice.store.production.controller.service

import com.amrtm.microservice.store.production.controller.repository.GroupRepository
import com.amrtm.microservice.store.production.controller.service.kafka.KafkaService
import com.amrtm.microservice.store.production.model.Group
import lombok.AllArgsConstructor
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.stereotype.Service
import java.util.*

@Service
@AllArgsConstructor
class GroupService {
    private lateinit var groupRepository: GroupRepository
    private lateinit var kafkaService: KafkaService
    private lateinit var errorAppsTopic: NewTopic
    private lateinit var globalAppsTopic: NewTopic
    // ElasticSearch
    get
    getAll
    getAll
    // MySQL
    fun store(data: Group) {
        try {
            groupRepository.save(data)
        } catch (err: Exception) {
            kafkaService.sendLog(errorAppsTopic,"error_group_store", err.localizedMessage,
                    globalAppsTopic, {
                msg -> msg.split("[A-Z]{9}".toRegex()).joinToString(prefix = "error:", postfix = "...")
            })
        }
    }
    fun delete(id: UUID) {
        try {
            groupRepository.deleteById(id)
        } catch (err: Exception) {
            kafkaService.sendLog(errorAppsTopic,"error_group_delete", err.localizedMessage,
                    globalAppsTopic, {
                msg -> msg.split("[A-Z]{9}".toRegex()).joinToString(prefix = "error:", postfix = "...")
            })
        }
    }
}
