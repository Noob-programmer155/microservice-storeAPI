package com.amrtm.microservice.store.production.model.soap.model

import com.amrtm.microservice.store.production.controller.configuration.listeners.soap.MainEntityListener
import com.amrtm.microservice.store.production.model.store_only.ProductPoint
import com.amrtm.microservice.store.production.model.store_only.ProductPrice
import com.amrtm.microservice.store.production.model.store_only.ProductStock
import jakarta.persistence.*
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement
import org.springframework.data.annotation.Id
import java.util.UUID

@Entity
@Table(name="product")
@XmlRootElement(name="product")
@EntityListeners(MainEntityListener::class)
data class Product(
    @Id @GeneratedValue(strategy = GenerationType.UUID) @get:XmlElement var id: UUID?,
    @Column(unique = true) @get:XmlElement var name: String = "",
    @ManyToOne(optional = false)
    @JoinColumn(name = "brand", nullable = false)
    @get:XmlElement var brand: Brand?= null,
    @get:XmlElement var description: String? = null,
    @ElementCollection
    @CollectionTable(name = "product_keywords", joinColumns = [JoinColumn(name = "product_id")])
    @get:XmlElement var keywords: MutableSet<String> = HashSet(),
    @ManyToOne(optional = false)
    @JoinColumn(name = "point", nullable = false)
    @get:XmlElement var point: ProductPoint ?= null,
    @ManyToOne(optional = false)
    @JoinColumn(name = "price", nullable = false)
    @get:XmlElement var price: ProductPrice ?= null,
    @ManyToOne(optional = false)
    @JoinColumn(name = "stock", nullable = false)
    @get:XmlElement var stock: ProductStock ?= null,
    @ManyToOne(optional = false)
    @JoinColumn(name = "group", nullable = false)
    @get:XmlElement var group: Group?= null,
    @get:XmlElement var disable: Boolean = true
)