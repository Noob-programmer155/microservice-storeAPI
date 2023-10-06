package com.amrtm.microservice.store.production.model.store_only

import com.amrtm.microservice.store.production.controller.configuration.listeners.soap.ProductPointEntityListener
import com.amrtm.microservice.store.production.controller.configuration.listeners.soap.transaction.EntityListenerProductPointSave
import com.amrtm.microservice.store.production.controller.configuration.listeners.soap.transaction.EntityListenerProductPointUpdate
import com.amrtm.microservice.store.production.model.Product
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import jakarta.persistence.*
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement
import jakarta.xml.bind.annotation.XmlTransient
import org.springframework.data.domain.DomainEvents
import java.util.*

@Entity
@Table(name="product_point")
@EntityListeners(ProductPointEntityListener::class)
@XmlRootElement(name = "product_point")
@JsonIgnoreProperties(ignoreUnknown = true)
class ProductPoint(
    @Id @GeneratedValue
    @get:XmlElement var id:Long ?= null,
    @JsonIgnore
    @get:XmlElement var product_id: UUID ?= null,
    @get:XmlElement var score: Float = 0f,
    @get:XmlElement var timestamp: Long,
    @JsonIgnore
    @XmlTransient
    @OneToMany(mappedBy = "point", cascade = [CascadeType.ALL], orphanRemoval = true)
    var products: MutableSet<Product> ?= null
) {
    constructor(): this(null,timestamp=Date().time)
    fun addProduct(product: Product) {
        if (this.products == null) this.products = mutableSetOf()
        this.product_id = product.id
        product.point = this
        if (this.products?.contains(product)!!) return
        this.products?.add(product)
    }
    fun removeProduct(product: Product) {
        if (this.products != null && !this.products?.contains(product)!!) return
        this.products?.remove(product)
        product.point = null
    }
    @DomainEvents
    fun domainEvents(): Collection<Any> {
        val events = mutableListOf<Any>()
        if (id == null) events.add(EntityListenerProductPointSave(this))
        else events.add(EntityListenerProductPointUpdate(this))
        return events
    }
}