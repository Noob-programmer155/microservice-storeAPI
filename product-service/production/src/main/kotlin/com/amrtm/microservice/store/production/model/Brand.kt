package com.amrtm.microservice.store.production.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Getter
import lombok.Setter

@Entity
@Builder
@AllArgsConstructor
class Brand {
    @Id
    @GeneratedValue
    @Getter @Setter var id: Long ?= null
    @Column(unique = true)
    @Getter @Setter lateinit var name: String
    @Getter @Setter var score: UInt = 1u
    @JsonIgnore
    @OneToMany(mappedBy = "brand", cascade = [CascadeType.MERGE, CascadeType.PERSIST])
    var products: MutableList<Product> = ArrayList()
    fun addProduct(product: Product) {
        this.products.add(product)
        product.brand = this
    }

    fun removeProduct(product: Product) {
        this.products.remove(product)
        product.brand = null
    }
}