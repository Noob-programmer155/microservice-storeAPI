package com.amrtm.microservice.store.production.kafka

import com.amrtm.microservice.store.production.controller.service.kafka.KafkaService
import com.amrtm.microservice.store.production.model.kafka.KafkaMessage
import org.apache.kafka.clients.admin.NewTopic
import org.junit.jupiter.api.Test
import org.junit.platform.commons.logging.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import java.util.concurrent.TimeUnit

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1 /*brokerProperties = ["listeners=PLAINTEXT://localhost:9092","port=9092"]*/)
class KafkaTest {
    val log = LoggerFactory.getLogger(KafkaTest::class.java)
    @Autowired
    private lateinit var kafkaService: KafkaService
    @Autowired
    private lateinit var globalAppsTopic: NewTopic

    @Test
    fun kafkaTestEnv() {
        kafkaService.sendLog(globalAppsTopic,"key1", KafkaMessage(1L,"hello there"))

        val wait = kafkaService.latch.await(10,TimeUnit.SECONDS)
        log.info { kafkaService.payload?.message }
        assert(kafkaService.payload?.message.equals("hello there"))
        assert(wait)
    }
}