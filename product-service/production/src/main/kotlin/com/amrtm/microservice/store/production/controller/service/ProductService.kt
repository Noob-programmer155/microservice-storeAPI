package com.amrtm.microservice.store.production.controller.service

import com.amrtm.microservice.store.production.controller.repository.ProductRepository
import com.amrtm.microservice.store.production.controller.repository.elastic.ProductRepositoryElastic
import com.amrtm.microservice.store.production.controller.repository.store_only.ProductPointRepository
import com.amrtm.microservice.store.production.controller.repository.store_only.ProductPriceRepository
import com.amrtm.microservice.store.production.controller.repository.store_only.ProductStockRepository
import com.amrtm.microservice.store.production.controller.service.kafka.KafkaService
import com.amrtm.microservice.store.production.model.Product
import com.amrtm.microservice.store.production.model.elastic.ProductElastic
import com.amrtm.microservice.store.production.model.filter.FilterProduct
import com.amrtm.microservice.store.production.model.filter.SearchProduct
import com.amrtm.microservice.store.production.model.kafka.KafkaMessage
import com.amrtm.microservice.store.production.model.properties.ProductProperties
import com.amrtm.microservice.store.production.model.store_only.ProductPoint
import com.amrtm.microservice.store.production.model.store_only.ProductPrice
import com.amrtm.microservice.store.production.model.store_only.ProductStock
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.math.BigInteger
import java.util.UUID

@Service
class ProductService(
    var productRepository: ProductRepository,
    var productPointRepository: ProductPointRepository,
    var productStockRepository: ProductStockRepository,
    var productPriceRepository: ProductPriceRepository,
    var productRepositoryElastic: ProductRepositoryElastic,
    var kafkaService: KafkaService,
    var errorAppsTopic: NewTopic,
    var globalAppsTopic: NewTopic
) {
    // ElasticSearch
    fun get(id:UUID):ProductElastic {
        // create
    }
    fun getAllSearch(pages: PageRequest, search: SearchProduct):List<ProductElastic> {

    }
    fun getAll(pages: PageRequest, filter: FilterProduct):List<ProductElastic> {

    }
    // MySQL
    fun addScore(id: UUID, score: UInt) {
        productPointRepository.save(ProductPoint(product = id, score = score))
    }
    fun addStock(id: UUID, stock: Long) {
        productStockRepository.save(ProductStock(product = id, stock= stock))
    }
    fun addPrice(id: UUID, price: Long) {
        productPriceRepository.save(ProductPrice(product = id, price = price))
    }
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