package com.amrtm.microservice.store.production.model.store_only

import com.amrtm.microservice.store.production.controller.configuration.listeners.soap.BrandPointEntityListener
import com.amrtm.microservice.store.production.controller.configuration.listeners.soap.transaction.EntityListenerBrandPointSave
import com.amrtm.microservice.store.production.controller.configuration.listeners.soap.transaction.EntityListenerBrandPointUpdate
import com.amrtm.microservice.store.production.model.Brand
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import jakarta.persistence.*
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement
import jakarta.xml.bind.annotation.XmlTransient
import org.springframework.data.domain.DomainEvents
import java.util.*

@Entity
@Table(name="brand_product_point")
@EntityListeners(BrandPointEntityListener::class)
@XmlRootElement(name = "brand_point")
@JsonIgnoreProperties(ignoreUnknown = true)
class BrandPoint(
    @Id @GeneratedValue
    @get:XmlElement var id:Long ?= null,
    @JsonIgnore
    @get:XmlElement var brand_id: Long ?= null,
    @get:XmlElement var score: Float = 0f,
    @get:XmlElement var timestamp: Long,
    @JsonIgnore
    @XmlTransient
    @OneToMany(mappedBy = "score", cascade = [CascadeType.ALL], orphanRemoval = true)
    var brands: MutableSet<Brand> ?= null
) {
    constructor(): this(id=null,timestamp=Date().time)
    fun addBrand(brand: Brand) {
        if (this.brands == null) this.brands = mutableSetOf()
        if (this.brands?.contains(brand)!!) return
        this.brand_id = brand.id
        this.brands?.add(brand)
        brand.score = this
    }
    fun removeBrand(brand: Brand) {
        if (this.brands != null && !this.brands?.contains(brand)!!) return
        this.brands?.remove(brand)
        brand.score = null
    }
    @DomainEvents
    fun domainEvents(): Collection<Any> {
        val events = mutableListOf<Any>()
        if (id == null) events.add(EntityListenerBrandPointSave(this))
        else events.add(EntityListenerBrandPointUpdate(this))
        return events
    }
}