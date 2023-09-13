package com.amrtm.microservice.store.production.controller.service.kafka

import com.amrtm.microservice.store.production.model.kafka.KafkaMessage
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.kafka.support.SendResult
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CountDownLatch
import java.util.function.Function

@Service
class KafkaService(var template: KafkaTemplate<String, KafkaMessage<Long, String>>) {
//    private lateinit var factory: StreamsBuilderFactoryBean
//    private lateinit var streamsBuilder: StreamsBuilder
    fun sendLog(topic: NewTopic, key:String, msg: KafkaMessage<Long, String>, globalTopic: NewTopic? = null,
                msgGlobalTopic: Function<KafkaMessage<Long, String>, KafkaMessage<Long, String>>? = null):
            CompletableFuture<SendResult<String, KafkaMessage<Long, String>>>? {
        return template.send(topic.name(),key,msg).whenComplete { _, u ->
            if (u != null && globalTopic != null)
                template.send(globalTopic.name(),key, msgGlobalTopic?.apply(msg) ?: msg)
        }
    }

    // test only
    var latch = CountDownLatch(1)
    var payload: KafkaMessage<Long,String>? = null
    var key = ""
    @KafkaListener(topics = ["Product_Apps"], containerFactory = "consumerContainer", groupId = "group1")
    fun listenMsg(@Payload message: KafkaMessage<Long,String>, @Header(KafkaHeaders.RECEIVED_KEY) key: String) {
        System.out.println("Received Message in group foo: " + message);
        this.payload = message
        this.key = key
        latch.countDown()
    }
}
