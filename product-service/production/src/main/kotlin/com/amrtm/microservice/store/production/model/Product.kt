package com.amrtm.microservice.store.production.model

import com.amrtm.microservice.store.production.controller.configuration.listeners.soap.MainEntityListener
import com.amrtm.microservice.store.production.model.store_only.ProductPoint
import com.amrtm.microservice.store.production.model.store_only.ProductPrice
import com.amrtm.microservice.store.production.model.store_only.ProductStock
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement
import java.util.UUID

@Entity
@Table(name="product")
@EntityListeners(MainEntityListener::class)
@XmlRootElement(name = "product")
@JsonIgnoreProperties(ignoreUnknown = true)
class Product(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @get:XmlElement var id: UUID ?= null,
    @Column(unique = true)
    @get:XmlElement var name: String = "",
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_brand", nullable = false)
    @get:XmlElement var brand: Brand ?= null,
    @get:XmlElement var description: String? = null,
    @ElementCollection
    @CollectionTable(name = "product_keywords", joinColumns = [JoinColumn(name = "product_id")])
    @get:XmlElement var keywords: MutableSet<String> = mutableSetOf(),
    @ManyToOne
    @JoinColumn(name = "point", nullable = true)
    @get:XmlElement var point: ProductPoint ?= null,
    @ManyToOne(cascade = [CascadeType.PERSIST,CascadeType.MERGE])
    @JoinColumn(name = "price", nullable = true)
    @get:XmlElement var price: ProductPrice ?= null,
    @ManyToOne(cascade = [CascadeType.PERSIST,CascadeType.MERGE])
    @JoinColumn(name = "stock", nullable = true)
    @get:XmlElement var stock: ProductStock ?= null,
    @ManyToOne(cascade = [CascadeType.PERSIST,CascadeType.MERGE])
    @JoinColumn(name = "product_group", nullable = false)
    @get:XmlElement var group: Group ?= null,
    @get:XmlElement var disable: Boolean = false
)