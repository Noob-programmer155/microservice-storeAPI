package com.amrtm.microservice.store.transformationandevaluationproduct.model.store_only

import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement
import java.util.*

@XmlRootElement(name = "product_stock")
data class ProductStock(
    @get:XmlElement var id:Long ?= null,
    @get:XmlElement var product_id: UUID ?= null,
    @get:XmlElement var stock: Long = 0,
    @get:XmlElement var timestamp: Long = Date().time
)