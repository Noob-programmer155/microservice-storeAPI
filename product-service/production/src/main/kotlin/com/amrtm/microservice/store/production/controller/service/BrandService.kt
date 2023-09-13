package com.amrtm.microservice.store.production.controller.service

import com.amrtm.microservice.store.production.controller.repository.BrandRepository
import com.amrtm.microservice.store.production.controller.service.kafka.KafkaService
import com.amrtm.microservice.store.production.model.Brand
import com.amrtm.microservice.store.production.model.kafka.KafkaMessage
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.stereotype.Service
import java.util.*

@Service
class BrandService(var brandRepository: BrandRepository, var kafkaService: KafkaService, var errorAppsTopic: NewTopic,
                   var globalAppsTopic: NewTopic) {
    // ElasticSearch
//    get
//    getAll
//    getAll
    // MySQL
    fun store(data: Brand) {
        try {
            brandRepository.save(data)
        } catch (err: Exception) {
            kafkaService.sendLog(errorAppsTopic,"error_brand_store",  KafkaMessage(1L,err.localizedMessage),
                    globalAppsTopic) { msg ->
                        KafkaMessage(1L, msg.message?.split("[A-Z]{9}".toRegex())
                        ?.joinToString(prefix = "error:", postfix = "..."))
            }
        }
    }
    fun delete(id: UUID) {
        try {
            brandRepository.deleteById(id)
        } catch (err: Exception) {
            kafkaService.sendLog(errorAppsTopic,"error_brand_delete", KafkaMessage(1L,err.localizedMessage),
                    globalAppsTopic) { msg ->
                KafkaMessage(1L, msg.message?.split("[A-Z]{9}".toRegex())
                        ?.joinToString(prefix = "error:", postfix = "..."))
            }
        }
    }
}