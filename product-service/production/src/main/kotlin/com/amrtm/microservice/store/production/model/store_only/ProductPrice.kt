package com.amrtm.microservice.store.production.model.store_only

import com.amrtm.microservice.store.production.controller.configuration.listeners.soap.ProductPriceEntityListener
import com.amrtm.microservice.store.production.model.Product
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement
import jakarta.xml.bind.annotation.XmlTransient
import java.util.*
import kotlin.collections.HashSet

@Entity
@Table(name="product_price")
@EntityListeners(ProductPriceEntityListener::class)
@XmlRootElement(name = "product_price")
@JsonIgnoreProperties(ignoreUnknown = true)
class ProductPrice(
    @Id @GeneratedValue
    @get:XmlElement var id:Long ?= null,
    @JsonIgnore
    @get:XmlElement var product_id: UUID ?= null,
    @get:XmlElement var price: Long = 0,
    @get:XmlElement var timestamp: Long,
    @JsonIgnore
    @XmlTransient
    @OneToMany(mappedBy = "price", cascade = [CascadeType.ALL], orphanRemoval = true)
    var products: MutableSet<Product> ?= null
) {
    constructor():this(null,timestamp=Date().time)
    fun addProduct(product: Product) {
        if (this.products == null) this.products = mutableSetOf()
        this.product_id = product.id
        product.price = this
        if (this.products?.contains(product)!!) return
        this.products?.add(product)
    }
    fun removeProduct(product: Product) {
        if (this.products != null && !this.products?.contains(product)!!) return
        this.products?.remove(product)
        product.price = null
    }
}