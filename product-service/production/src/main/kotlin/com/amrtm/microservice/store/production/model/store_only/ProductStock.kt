package com.amrtm.microservice.store.production.model.store_only

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigInteger
import java.util.*

@Entity
@Table(name="product_stock")
class ProductStock (
    @Id
    @GeneratedValue var id:Long ?= null,
    var product: UUID,
    var stock: Long,
    var timestamp: Long = Date().time
)