package com.amrtm.microservice.store.production.model.properties

enum class ProductProperties(val properties: String) {
    NAME("name"),
    BRAND("brand"),
    SCORE("score"),
    STOCK("stock"),
    PRICE("price"),
    GROUP("group"),
    DISABLE("disable");

    fun properties(): String {
        return this.properties
    }
}