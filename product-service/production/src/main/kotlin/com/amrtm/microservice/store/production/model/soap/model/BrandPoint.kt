package com.amrtm.microservice.store.production.model.soap.model

import com.amrtm.microservice.store.production.controller.configuration.listeners.soap.BrandPointEntityListener
import com.amrtm.microservice.store.production.model.Brand
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlTransient
import java.util.*
import kotlin.collections.HashSet

@Entity
@Table(name="brand_product_point")
@EntityListeners(BrandPointEntityListener::class)
class BrandPoint (
    @Id
    @GeneratedValue var id:Long ?= null,
    @JsonIgnore
    @get:XmlElement var brand_id: Long ?= null,
    @get:XmlElement var score: UInt,
    @get:XmlElement var timestamp: Long = Date().time,
    @JsonIgnore
    @XmlTransient
    @OneToMany(mappedBy = "score") var brands: MutableSet<Brand> = HashSet()
) {
    fun addBrand(brand: Brand) {
        this.brands.add(brand)
        brand.score = this
    }
    fun removeProduct(brand: Brand) {
        this.brands.remove(brand)
        brand.score = null
    }
}