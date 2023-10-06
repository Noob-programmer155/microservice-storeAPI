package com.amrtm.microservice.store.production.model.filter

data class FilterProduct(
    var keywords: MutableSet<String>,
    var priceStart: Long,
    var priceEnd: Long,
    var scoreStart: Float,
    var scoreEnd: Float,
    var brandScoreStart: Float,
    var brandScoreEnd: Float,
    var groupType: String
)
