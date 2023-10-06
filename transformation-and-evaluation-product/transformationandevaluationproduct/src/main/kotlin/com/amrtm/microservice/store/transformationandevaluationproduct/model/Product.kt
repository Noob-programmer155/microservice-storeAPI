package com.amrtm.microservice.store.transformationandevaluationproduct.model

import com.amrtm.microservice.store.transformationandevaluationproduct.model.store_only.ProductPoint
import com.amrtm.microservice.store.transformationandevaluationproduct.model.store_only.ProductPrice
import com.amrtm.microservice.store.transformationandevaluationproduct.model.store_only.ProductStock
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement
import java.util.UUID

@XmlRootElement(name = "product")
data class Product(
    @get:XmlElement var id: UUID ?= null,
    @get:XmlElement var name: String = "",
    @get:XmlElement var brand: Brand ?= null,
    @get:XmlElement var description: String? = null,
    @get:XmlElement var keywords: MutableSet<String> = HashSet(),
    @get:XmlElement var point: ProductPoint?= null,
    @get:XmlElement var price: ProductPrice?= null,
    @get:XmlElement var stock: ProductStock?= null,
    @get:XmlElement var group: Group ?= null,
    @get:XmlElement var disable: Boolean = false
)