package com.amrtm.microservice.store.production.model.soap.model

import com.amrtm.microservice.store.production.controller.configuration.listeners.BrandEntityListener
import com.amrtm.microservice.store.production.model.store_only.BrandPoint
import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlTransient

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator::class)
@EntityListeners(BrandEntityListener::class)
@Table(name="brand")
data class BrandSOAP(
    @Id @GeneratedValue
    @get:XmlElement var id: Long?,
    @Column(unique = true)
    @get:XmlElement var name: String = "",
    @ManyToOne(optional = false)
    @JoinColumn(name = "score", nullable = false)
    @get:XmlElement var score: BrandPoint?,
    @JsonIgnore
    @XmlTransient
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