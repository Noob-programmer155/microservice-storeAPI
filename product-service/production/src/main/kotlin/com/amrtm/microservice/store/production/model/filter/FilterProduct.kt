package com.amrtm.microservice.store.production.model.filter

data class FilterProduct(
    var keywords: MutableSet<String>,
    var priceStart: Long,
    var priceEnd: Long,
    var scoreStart: UInt,
    var scoreEnd: UInt,
    var brandScoreStart: UInt,
    var brandScoreEnd: UInt,
    var groupType: String
)
