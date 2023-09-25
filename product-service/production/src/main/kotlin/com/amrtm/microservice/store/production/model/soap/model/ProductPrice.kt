package com.amrtm.microservice.store.production.model.soap.model

import com.amrtm.microservice.store.production.controller.configuration.listeners.soap.ProductPriceEntityListener
import com.amrtm.microservice.store.production.model.Product
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlTransient
import java.util.*
import kotlin.collections.HashSet

@Entity
@Table(name="product_price")
@EntityListeners(ProductPriceEntityListener::class)
class ProductPrice (
    @Id
    @GeneratedValue var id:Long ?= null,
    @JsonIgnore
    @get:XmlElement var product_id: UUID ?= null,
    @get:XmlElement var price: Long,
    @get:XmlElement var timestamp: Long = Date().time,
    @JsonIgnore
    @XmlTransient
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