package com.amrtm.microservice.store.production.model.store_only

import com.amrtm.microservice.store.production.controller.configuration.listeners.soap.ProductPriceEntityListener
import com.amrtm.microservice.store.production.model.Product
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.util.*
import kotlin.collections.HashSet

@Entity
@Table(name="product_price")
@EntityListeners(ProductPriceEntityListener::class)
class ProductPrice (
    @Id
    @GeneratedValue var id:Long ?= null,
    @JsonIgnore
    var product_id: UUID ?= null,
    var price: Long,
    var timestamp: Long = Date().time,
    @JsonIgnore
    @OneToMany(mappedBy = "price") var products: MutableSet<Product> = HashSet()
) {
    fun addProduct(product: Product) {
        this.products.add(product)
        product.price = this
    }
    fun removeProduct(product: Product) {
        this.products.remove(product)
        product.price = null
    }
}