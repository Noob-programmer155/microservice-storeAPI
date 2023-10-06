package com.amrtm.microservice.store.transformationandevaluationproduct.model

import com.amrtm.microservice.store.transformationandevaluationproduct.model.store_only.BrandPoint
import jakarta.xml.bind.annotation.XmlElement

data class Brand(
    @get:XmlElement var id: Long?,
    @get:XmlElement var name: String = "",
    @get:XmlElement var score: BrandPoint?,
)