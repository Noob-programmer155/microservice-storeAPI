package com.amrtm.microservice.store.production.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
class Brand {
    @Id
    @GeneratedValue
    var id: Long ?= null
    @Column(unique = true)
    var name: String
    var score: UInt = 1u
    @JsonIgnore
    @OneToMany(mappedBy = "brand", cascade = [CascadeType.MERGE, CascadeType.PERSIST])
    var products: MutableList<Product> = ArrayList()

    constructor(id: Long?, name: String, score: UInt, products: MutableList<Product>) {
        this.id = id
        this.name = name
        this.score = score
        this.products = products
    }

    fun addProduct(product: Product) {
        this.products.add(product)
        product.brand = this
    }

    fun removeProduct(product: Product) {
        this.products.remove(product)
        product.brand = null
    }
}