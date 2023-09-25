package com.amrtm.microservice.store.production.controller.configuration

import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.admin.AdminClientConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.KafkaAdmin

@Configuration
@EnableKafka
class KafkaConfig(@Value("\${spring.kafka.bootstrap-servers}") val server: String) {
//    @Bean(name=[KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME])
//    fun kafkaStreams() : KafkaStreamsConfiguration {
//        val props: MutableMap<String,Any> = HashMap()
//        props.put(APPLICATION_ID_CONFIG,"product-service")
//        props.put(BOOTSTRAP_SERVERS_CONFIG,server)
//        props.put(DEFAULT_KEY_SERDE_CLASS_CONFIG,Serdes.String().javaClass.name)
//        props.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG,Serdes.String().javaClass.name)
//        return KafkaStreamsConfiguration(props)
//    }

    @Bean
    fun kafkaAdmin(): KafkaAdmin {
        val props: MutableMap<String,Any> = HashMap()
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,server)
        return KafkaAdmin(props)
    }

    @Bean
    fun errorAppsTopic(): NewTopic {
        val topicConfigs = HashMap<String,String>();
        topicConfigs.put("retention.ms", "${86400000/(24*60)}");
        return NewTopic("Product_Apps_Error",1,1)
            .configs(topicConfigs)
    }
    @Bean
    fun sqlAppsTopic(): NewTopic {
        val topicConfigs = HashMap<String,String>();
        topicConfigs.put("retention.ms", "${86400000/(24*60)}");
        return NewTopic("Product_Apps_SQL",1,1)
            .configs(topicConfigs)
    }

    @Bean
    fun globalAppsTopic(): NewTopic {
        val topicConfigs = HashMap<String,String>();
        topicConfigs.put("retention.ms", "${86400000/(24*60)}");
        return NewTopic("Product_Apps",1,1)
            .configs(topicConfigs)
    }

//    @Bean
//    fun streamBuilder(): StreamsBuilder {
//        return StreamsBuilder()
//    }
}