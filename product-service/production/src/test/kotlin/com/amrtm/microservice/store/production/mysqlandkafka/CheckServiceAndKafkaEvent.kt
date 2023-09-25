package com.amrtm.microservice.store.production.mysqlandkafka

import com.amrtm.microservice.store.production.controller.service.ProductService
import com.amrtm.microservice.store.production.model.Product
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import java.util.logging.Logger

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1 /*brokerProperties = ["listeners=PLAINTEXT://localhost:9092","port=9092"]*/)
class CheckServiceAndKafkaEvent{
    @Autowired
    lateinit var productService: ProductService

    val logtest = Logger.getAnonymousLogger()
    @Test
    fun test() {
        val product = """
           {
              "id": null,
              "name": "product1",
              "brand": {
                "id": null,
                "name": "LXC",
                "score": {
                    "id": null,
                    "score": 0
                }
              },
              "description": "this is product 1",
              "keywords": ["key1","key2"],
              "point": {
                "id": null,
                "score": 0
              },
              "price": {
                "id": null,
                "price": 0
              },
              "stock": {
                "id": null,
                "stock": 250
              },
              "group": {
                "id": null,
                "name": "group1",
                "type": "type1",
              },
              "disable": false
           }
        """

        var mapper = ObjectMapper().readValue(product,Product::class.java)
        logtest.info(mapper.toString())
    }
    @Test
    fun servicePost() {
        val product = """
           {
              "id": null,
              "name": "product1",
              "brand": {
                "id": null,
                "name": "LXC",
                "score": {
                    "id": null,
                    "score": 0
                }
              },
              "description": "this is product 1",
              "keywords": ["key1","key2"],
              "point": {
                "id": null,
                "score": 0
              },
              "price": {
                "id": null,
                "price": 0
              },
              "stock": {
                "id": null,
                "stock": 250
              },
              "group": {
                "id": null,
                "name": "group1",
                "type": "type1",
              },
              "disable": false
           }
        """

        var mapper = ObjectMapper().readValue(product,Product::class.java)


//        productService.store()
//
//        productService.store()
//
//        productService.addBrandScore()
//
//        productService.addPrice()
//
//        productService.addScore()
//
//        productService.addStock()
//
//        productService.deleteBrand()
//
//        productService.deleteGroup()
//
//        productService.delete()
    }
//    @AfterAll
//    fun deletell() {
//
//    }
}