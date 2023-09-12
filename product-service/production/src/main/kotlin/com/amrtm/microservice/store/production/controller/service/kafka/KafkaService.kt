package com.amrtm.microservice.store.production.controller.service.kafka

import lombok.AllArgsConstructor
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.function.Function

@Service
@AllArgsConstructor
class KafkaService {
//    private lateinit var factory: StreamsBuilderFactoryBean
//    private lateinit var streamsBuilder: StreamsBuilder
    private lateinit var template: KafkaTemplate<String, String>

    fun sendLog(topic: NewTopic, key:String, msg: String) {
        template.send(topic.name(),key,msg)
    }

    fun sendLog(topic: NewTopic, key:String, msg: String, globalTopic: NewTopic?,
                msgGlobalTopic: Function<String,String>?) {
        template.send(topic.name(),key,msg).whenComplete { t, u ->
            if (u != null && globalTopic != null)
                template.send(globalTopic.name(),key,if (msgGlobalTopic != null) msgGlobalTopic.apply(msg) else msg)
        }
    }
}