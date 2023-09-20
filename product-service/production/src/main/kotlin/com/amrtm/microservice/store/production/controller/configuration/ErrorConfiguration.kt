package com.amrtm.microservice.store.production.controller.configuration

import com.amrtm.microservice.store.production.controller.service.kafka.KafkaService
import com.amrtm.microservice.store.production.model.kafka.KafkaMessage
import jakarta.servlet.ServletException
import jakarta.servlet.UnavailableException
import org.apache.kafka.clients.admin.NewTopic
import org.hibernate.HibernateException
import org.hibernate.MappingException
import org.springframework.beans.InvalidPropertyException
import org.springframework.beans.factory.BeanCreationException
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ErrorConfiguration: ResponseEntityExceptionHandler() {
    @Autowired lateinit var kafkaService: KafkaService
    @Autowired lateinit var errorAppsTopic: NewTopic
    @Autowired lateinit var globalAppsTopic: NewTopic
    private fun kafkaCreate(err: String) {
        kafkaService.sendLog(errorAppsTopic,"error_product_store", KafkaMessage(1L,err),
            globalAppsTopic) { msg ->
            KafkaMessage(1L, msg.message?.split("[A-Z]{9}".toRegex())
                ?.joinToString(prefix = "error:", postfix = "..."))
        }
    }

    @ExceptionHandler(value = [
        ServletException::class, UnavailableException::class, BeanCreationException::class,
        NoSuchBeanDefinitionException::class, DataAccessException::class, InvalidPropertyException::class,
        HibernateException::class, MappingException::class, RuntimeException::class,
        HttpServerErrorException::class, Exception::class
    ])
    protected fun errorHandler1(ex: Exception, request: WebRequest): ResponseEntity<Any>? {
        kafkaCreate(ex.stackTraceToString())
        return handleExceptionInternal(ex, ex.message, HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request)
    }

    @ExceptionHandler(value = [
        IllegalArgumentException::class, IllegalStateException::class, MethodArgumentNotValidException::class,
        HttpClientErrorException::class, HttpMessageNotReadableException::class
    ])
    protected fun errorHandler2(ex: Exception, request: WebRequest): ResponseEntity<Any>? {
        kafkaCreate(ex.stackTraceToString())
        return handleExceptionInternal(ex, ex.message, HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }
}