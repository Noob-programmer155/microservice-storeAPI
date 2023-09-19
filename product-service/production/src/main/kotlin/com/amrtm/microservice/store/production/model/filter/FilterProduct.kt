package com.amrtm.microservice.store.production.model.filter

data class FilterProduct(
    var name: String,
    var descriptionKeys: Array<String>,
    var prices: Array<Long>,
    var scores: Array<UInt>,
    var brandName: String,
    var brandScores: Array<UInt>,
    var groupName: String,
    var groupType: String
)
