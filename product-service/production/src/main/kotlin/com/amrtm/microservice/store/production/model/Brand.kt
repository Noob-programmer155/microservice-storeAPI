package com.amrtm.microservice.store.production.model

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator::class)
@Table(name="brand")
data class Brand(
    @Id var id: Long,
    @Column(unique = true) var name: String = "",
    @JsonIgnore
    @OneToMany(
        mappedBy = "brand",
        cascade = [CascadeType.MERGE, CascadeType.PERSIST]
    ) var products: MutableList<Product> = ArrayList()
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