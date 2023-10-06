package com.amrtm.microservice.store.production

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories("com.amrtm.microservice.store.production.controller.repository")
class ProductionApplication

fun main(args: Array<String>) {
	runApplication<ProductionApplication>(*args)
}

