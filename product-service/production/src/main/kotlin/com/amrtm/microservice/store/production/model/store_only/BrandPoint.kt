package com.amrtm.microservice.store.production.model.store_only

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name="brand_product_point")
class BrandPoint (
    @Id
    @GeneratedValue var id:Long ?= null,
    var product: UUID,
    var score: UInt,
    var timestamp: Long = Date().time
)