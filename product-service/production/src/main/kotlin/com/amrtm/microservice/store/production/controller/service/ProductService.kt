package com.amrtm.microservice.store.production.controller.service

import com.amrtm.microservice.store.production.controller.repository.ProductRepository
import com.amrtm.microservice.store.production.controller.service.kafka.KafkaService
import com.amrtm.microservice.store.production.model.Product
import com.amrtm.microservice.store.production.model.properties.ProductProperties
import lombok.AllArgsConstructor
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.UUID

@Service
@AllArgsConstructor
class ProductService {
    private lateinit var productRepository: ProductRepository
    private lateinit var kafkaService: KafkaService
    private lateinit var errorAppsTopic: NewTopic
    private lateinit var globalAppsTopic: NewTopic
    // ElasticSearch
    fun get(id:UUID):Product {}
    fun getAll(pages: PageRequest) {}
    fun getAll(pages: PageRequest, filter: Map<ProductProperties,*>) {}
    // MySQL
    fun store(data:Product) {
        try {
            productRepository.save(data)
        } catch (err: Exception) {
            kafkaService.sendLog(errorAppsTopic,"error_product_store", err.localizedMessage,
                    globalAppsTopic, {
                        msg -> msg.split("[A-Z]{9}".toRegex()).joinToString(prefix = "error:", postfix = "...")
                    })
        }
    }
    fun delete(id:UUID) {
        try {
            productRepository.deleteById(id)
        } catch (err: Exception) {
            kafkaService.sendLog(errorAppsTopic,"error_product_delete", err.localizedMessage,
                    globalAppsTopic, {
                msg -> msg.split("[A-Z]{9}".toRegex()).joinToString(prefix = "error:", postfix = "...")
            })
        }
    }
}