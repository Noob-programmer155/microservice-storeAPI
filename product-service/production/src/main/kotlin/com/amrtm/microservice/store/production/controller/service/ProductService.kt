package com.amrtm.microservice.store.production.controller.service

import com.amrtm.microservice.store.production.controller.repository.ProductRepository
import com.amrtm.microservice.store.production.controller.service.kafka.KafkaService
import com.amrtm.microservice.store.production.model.Product
import com.amrtm.microservice.store.production.model.kafka.KafkaMessage
import com.amrtm.microservice.store.production.model.properties.ProductProperties
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ProductService(var productRepository: ProductRepository, var kafkaService: KafkaService,
                     var errorAppsTopic: NewTopic, var globalAppsTopic: NewTopic) {
    // ElasticSearch
//    fun get(id:UUID):Product {}
    fun getAll(pages: PageRequest) {}
    fun getAll(pages: PageRequest, filter: Map<ProductProperties,*>) {}
    // gRPC
    fun addScore() {

    }
    fun removeStock() {

    }
    // MySQL
    fun store(data:Product) {
        try {
            productRepository.save(data)
        } catch (err: Exception) {
            kafkaService.sendLog(errorAppsTopic,"error_product_store", KafkaMessage(1L,err.localizedMessage),
                    globalAppsTopic) { msg ->
                KafkaMessage(1L, msg.message?.split("[A-Z]{9}".toRegex())
                        ?.joinToString(prefix = "error:", postfix = "..."))
            }
        }
    }
    fun delete(id:UUID) {
        try {
            productRepository.deleteById(id)
        } catch (err: Exception) {
            kafkaService.sendLog(errorAppsTopic,"error_product_delete", KafkaMessage(1L,err.localizedMessage),
                    globalAppsTopic) { msg ->
                KafkaMessage(1L, msg.message?.split("[A-Z]{9}".toRegex())
                        ?.joinToString(prefix = "error:", postfix = "..."))
            }
        }
    }
}