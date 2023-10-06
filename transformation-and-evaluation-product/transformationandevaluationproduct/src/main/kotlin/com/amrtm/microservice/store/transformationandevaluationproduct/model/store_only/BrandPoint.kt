package com.amrtm.microservice.store.transformationandevaluationproduct.model.store_only

import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement
import java.util.*

@XmlRootElement(name = "brand_point")
data class BrandPoint(
    @get:XmlElement var id:Long ?= null,
    @get:XmlElement var brand_id: Long ?= null,
    @get:XmlElement var score: UInt = 0u,
    @get:XmlElement var timestamp: Long = Date().time
)