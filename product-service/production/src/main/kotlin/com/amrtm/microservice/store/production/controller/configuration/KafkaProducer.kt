package com.amrtm.microservice.store.production.controller.configuration

import lombok.AllArgsConstructor
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

@Configuration
@AllArgsConstructor
class KafkaProducer(@Value("\${spring.kafka.bootstrap-servers}") val server: String) {

    @Bean
    fun producerFactory():ProducerFactory<String,String>{
        val props: MutableMap<String,Any> = HashMap()
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,server)
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,StringSerializer::class)
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer::class)
        return DefaultKafkaProducerFactory(props)
    }

    @Bean
    fun template(): KafkaTemplate<String,String> {
        return KafkaTemplate(producerFactory())
    }
}

//@AllArgsConstructor
//class KafkaPipeline {
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