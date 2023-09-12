package com.amrtm.microservice.store.production.model.properties

enum class GroupProperties(val properties: String) {
    NAME("name"),
    TYPE("type"),
    SUBGROUP("subgroup");

    fun properties(): String {
        return this.properties
    }
}