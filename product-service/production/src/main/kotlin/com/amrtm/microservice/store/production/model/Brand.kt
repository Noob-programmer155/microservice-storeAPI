package com.amrtm.microservice.store.production.model

import com.amrtm.microservice.store.production.controller.configuration.listeners.BrandEntityListener
import com.amrtm.microservice.store.production.model.store_only.BrandPoint
import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator::class)
@EntityListeners(BrandEntityListener::class)
@Table(name="brand")
data class Brand(
    @Id @GeneratedValue
    var id: Long?,
    @Column(unique = true)
    var name: String = "",
    @ManyToOne(optional = false)
    @JoinColumn(name = "score", nullable = false)
    var score: BrandPoint?,
    @JsonIgnore
    @OneToMany(
        mappedBy = "brand",
        cascade = [CascadeType.MERGE, CascadeType.PERSIST]
    ) var products: MutableSet<Product> = HashSet()
) {
    fun addProduct(product: Product) {
        this.products.add(product)
        product.brand = this
    }
    fun removeProduct(product: Product) {
        this.products.remove(product)
        product.brand = null
    }
}