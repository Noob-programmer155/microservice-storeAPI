package com.amrtm.microservice.store.production.model.store_only

import com.amrtm.microservice.store.production.controller.configuration.listeners.soap.BrandPointEntityListener
import com.amrtm.microservice.store.production.model.Brand
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.util.*
import kotlin.collections.HashSet

@Entity
@Table(name="brand_product_point")
@EntityListeners(BrandPointEntityListener::class)
class BrandPoint (
    @Id
    @GeneratedValue var id:Long ?= null,
    @JsonIgnore
    var brand_id: Long ?= null,
    var score: UInt,
    var timestamp: Long = Date().time,
    @JsonIgnore
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