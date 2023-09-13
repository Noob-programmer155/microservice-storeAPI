package com.amrtm.microservice.store.production.controller.configuration

import com.amrtm.microservice.store.production.model.kafka.KafkaMessage
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer
import java.util.concurrent.ConcurrentLinkedQueue

@Configuration
class KafkaProducer(@Value("\${spring.kafka.bootstrap-servers}") val server: String) {

    @Bean
    fun <K,V> producerFactory():ProducerFactory<String,KafkaMessage<K,V>>{
        val props: MutableMap<String,Any> = HashMap()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = server
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java
        return DefaultKafkaProducerFactory(props)
    }

    @Bean
    fun template(): KafkaTemplate<String,KafkaMessage<Long,String>> {
        return KafkaTemplate(producerFactory())
    }

    // Test Only
    @Bean
    fun <K,V> consumerFactory(): ConsumerFactory<String,KafkaMessage<K,V>> {
        val props: MutableMap<String,Any> = HashMap()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = server
        return DefaultKafkaConsumerFactory(
                props,
                StringDeserializer(),
                JsonDeserializer(KafkaMessage::class.java)
        )
    }

    @Bean
    fun consumerContainer(): ConcurrentKafkaListenerContainerFactory<String,KafkaMessage<Long,String>> {
        val container = ConcurrentKafkaListenerContainerFactory<String,KafkaMessage<Long,String>>()
        container.consumerFactory = consumerFactory()
        return container
    }
}

//class KafkaPipeline() {
//    private lateinit var streamsBuilder: StreamsBuilder
//    private lateinit var errorAppsTopic: NewTopic
//    private lateinit var sqlAppsTopic: NewTopic
//    private lateinit var globalAppsTopic: NewTopic
//    private val SERDE = Serdes.String()
//
//    fun errorAppsStreams() {
//        streamsBuilder.stream(errorAppsTopic.name(), Consumed.with(SERDE,SERDE))
//                .mapValues{value -> value.split("[A-Z]{9}".toRegex()).joinToString(prefix = "error:", postfix = "...")}
//                .groupByKey()
//                .count(Materialized.`as`("errorCounts"))
//                .toStream()
//                .to(globalAppsTopic.name())
//    }
//    fun sqlAppsStreams() {
//        streamsBuilder.stream(sqlAppsTopic.name(),Consumed.with(SERDE,SERDE))
//                .groupByKey()
//                .count(Materialized.`as`("sqlCounts"))
//                .toStream()
//                .to(globalAppsTopic.name())
//    }
//}