package com.amrtm.microservice.store.production.controller.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.oxm.jaxb.Jaxb2Marshaller
import org.springframework.ws.client.core.WebServiceTemplate

@Configuration
class SOAPConfig {
    @Value("\${soap.target.domain}")
    lateinit var url: String

    @Bean
    fun marshaller(): Jaxb2Marshaller {
        val marsh = Jaxb2Marshaller()
        marsh.contextPath = "com.amrtm.microservice.store.production.model"
        return marsh
    }

    @Bean
    fun webServiceTemplate(marshaller: Jaxb2Marshaller): WebServiceTemplate {
        val service = WebServiceTemplate()
        service.marshaller = marshaller
        service.unmarshaller = marshaller
        service.defaultUri = url
        return service
    }
}