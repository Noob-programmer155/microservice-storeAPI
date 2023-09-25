package com.amrtm.microservice.store.production.model.store_only

import com.amrtm.microservice.store.production.controller.configuration.listeners.soap.ProductStockEntityListener
import com.amrtm.microservice.store.production.model.Product
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.util.*
import kotlin.collections.HashSet

@Entity
@Table(name="product_stock")
@EntityListeners(ProductStockEntityListener::class)
class ProductStock (
    @Id
    @GeneratedValue var id:Long ?= null,
    @JsonIgnore
    var product_id: UUID ?= null,
    var stock: Long,
    var timestamp: Long = Date().time,
    @JsonIgnore
    @OneToMany(mappedBy = "stock") var products: MutableSet<Product> = HashSet()
) {
    fun addProduct(product: Product) {
        this.products.add(product)
        product.stock = this
    }
    fun removeProduct(product: Product) {
        this.products.remove(product)
        product.stock = null
    }
}