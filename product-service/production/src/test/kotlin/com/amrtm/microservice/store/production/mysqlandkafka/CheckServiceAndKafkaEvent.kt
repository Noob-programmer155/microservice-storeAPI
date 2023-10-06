package com.amrtm.microservice.store.production.mysqlandkafka

import com.amrtm.microservice.store.production.controller.service.ProductService
import com.amrtm.microservice.store.production.model.Brand
import com.amrtm.microservice.store.production.model.Group
import com.amrtm.microservice.store.production.model.Product
import com.amrtm.microservice.store.production.model.store_only.BrandPoint
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.transaction.Transactional
import org.hibernate.Hibernate
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import java.util.Date
import java.util.logging.Logger

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1 /*brokerProperties = ["listeners=PLAINTEXT://localhost:9092","port=9092"]*/)
@Transactional
class CheckServiceAndKafkaEvent{
    val log = Logger.getGlobal()
    @Autowired
    lateinit var productService: ProductService
    @PersistenceContext
    lateinit var entityManager: EntityManager

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
                    "score": 4
                }
              },
              "description": "this is product 1",
              "keywords": ["key1","key2"],
              "point": {
                "id": null,
                "score": 4
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
                "type": "type1"
              },
              "disable": false
           }
        """

        val mapper = ObjectMapper().readValue(product,Product::class.java)
        assert(mapper.id == null)
        assert(mapper.name == "product1")
        assert(mapper.brand?.score?.score == 4f)
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
                    "score": 4.0
                }
              },
              "description": "this is product 1",
              "keywords": ["key1","key2"],
              "point": {
                "id": null,
                "score": 4.0
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
                "type": "type1"
              },
              "disable": false
           }
        """

        val mapper = ObjectMapper().readValue(product,Product::class.java)

        val data1 = productService.store(mapper)
        entityManager.refresh(data1)
        data1.group = Group(name = "group2", type = "type2")
        data1.brand = Brand(name = "LCY", score = BrandPoint(score = 4f, timestamp = Date().time))

        val data2 = productService.store(data1)
        entityManager.refresh(data2)

        val grp1 = productService.groupRepository.findByName("group1")
        val grp2 = productService.groupRepository.findByName("group2")
        entityManager.refresh(grp2)
        grp2.addSubgroup(grp1)
        data2.group = grp2

        val data4 = productService.store(data2)

        productService.addBrandScore(data4.brand?.id!!,4u)

        productService.addPrice(data4.id!!,1000L)

        productService.addScore(data4.id!!,4u)

        productService.addStock(data4.id!!, 200L)

        val data5 = productService.productRepository.findById(data4.id!!).orElseThrow()
        log.info(data5.brand?.score?.score!!.toString())
        log.info(data5.price?.price!!.toString())
        log.info(data5.point?.score!!.toString())
        log.info(data5.stock?.stock!!.toString())
        assert(data5.brand?.score?.score!! == 4f)
        assert(data5.price?.price!! == 1000L)
        assert(data5.point?.score!! == 4f)
        assert(data5.stock?.stock!! == 200L)

        productService.delete(data5.id!!)

        productService.deleteBrand(data5.brand?.id!!)

        productService.deleteGroup(grp1.id!!)

        productService.deleteGroup(grp2.id!!)

        val data6 = productService.productRepository.findById(data5.id!!)
        assert(data6.isEmpty)
    }
}