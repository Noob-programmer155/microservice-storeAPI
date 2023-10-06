package com.amrtm.microservice.store.production.controller.configuration

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient
import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.json.jackson.JacksonJsonpMapper
import co.elastic.clients.json.jsonb.JsonbJsonpMapper
import co.elastic.clients.transport.ElasticsearchTransport
import co.elastic.clients.transport.rest_client.RestClientTransport
import org.apache.http.HttpHost
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.impl.client.BasicCredentialsProvider
import org.elasticsearch.client.RestClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ElasticConfig {
    @Value("\${ELASTIC_URI}")
    lateinit var url: String
    @Value("\${ELASTIC_USERNAME}")
    lateinit var username: String
    @Value("\${ELASTIC_PASSWORD}")
    lateinit var password: String

    private fun provider(): BasicCredentialsProvider {
        val cred = BasicCredentialsProvider()
        cred.setCredentials(AuthScope.ANY,UsernamePasswordCredentials(username,password))
        return cred
    }

    @Bean
    fun elasticsearchClientv2(): ElasticsearchAsyncClient {
        val client = RestClient
            .builder(HttpHost.create(url))
            .setHttpClientConfigCallback {
                it.setDefaultCredentialsProvider(provider())
            }.build()
        val transport = RestClientTransport(client,JacksonJsonpMapper())
        return ElasticsearchAsyncClient(transport)
    }
}