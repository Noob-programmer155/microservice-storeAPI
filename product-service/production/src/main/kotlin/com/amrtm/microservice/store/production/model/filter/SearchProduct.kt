package com.amrtm.microservice.store.production.model.filter

data class SearchProduct(
    var name: String,
    var price: Long,
    var score: UInt,
    var brandName: String,
    var brandScore: UInt,
    var groupName: String,
    var groupType: String
)