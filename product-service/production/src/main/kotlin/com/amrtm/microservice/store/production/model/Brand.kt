package com.amrtm.microservice.store.production.model

import com.amrtm.microservice.store.production.controller.configuration.listeners.BrandEntityListener
import com.amrtm.microservice.store.production.model.store_only.BrandPoint
import com.fasterxml.jackson.annotation.*
import jakarta.persistence.*
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlTransient

@Entity
@Table(name="brand")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "id")
@EntityListeners(BrandEntityListener::class)
@JsonIgnoreProperties(ignoreUnknown = true)
class Brand(
    @Id @GeneratedValue
    @get:XmlElement var id: Long ?= null,
    @Column(unique = true)
    @get:XmlElement var name: String = "",
    @ManyToOne(cascade = [CascadeType.PERSIST,CascadeType.MERGE])
    @JoinColumn(name = "score", nullable = true)
    @get:XmlElement var score: BrandPoint ?= null,
    @JsonIgnore
    @XmlTransient
    @OneToMany(mappedBy = "brand", cascade = [CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH]) var products: MutableSet<Product> ?= null
) {
    fun addProduct(product: Product) {
        if (this.products == null) this.products = mutableSetOf()
        if (this.products?.contains(product)!!) return
        this.products?.add(product)
        product.brand = this
    }
    fun removeProduct(product: Product) {
        if (this.products != null && !this.products?.contains(product)!!) return
        this.products?.remove(product)
        product.brand = null
    }
}