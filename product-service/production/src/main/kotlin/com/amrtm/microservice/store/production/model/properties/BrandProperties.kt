package com.amrtm.microservice.store.production.model.properties

enum class BrandProperties(val properties: String) {
    NAME("name"),
    SCORE("score");

    fun properties(): String {
        return this.properties
    }
}