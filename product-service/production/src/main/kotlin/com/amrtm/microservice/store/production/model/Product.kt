package com.amrtm.microservice.store.production.model

import com.amrtm.microservice.store.production.controller.configuration.listeners.soap.MainEntityListener
import com.amrtm.microservice.store.production.model.store_only.ProductPoint
import com.amrtm.microservice.store.production.model.store_only.ProductPrice
import com.amrtm.microservice.store.production.model.store_only.ProductStock
import jakarta.persistence.*
import org.springframework.data.annotation.Id
import java.util.UUID

@Entity
@Table(name="product")
@EntityListeners(MainEntityListener::class)
data class Product(
    @Id @GeneratedValue(strategy = GenerationType.UUID) var id: UUID?,
    @Column(unique = true) var name: String = "",
    @ManyToOne(optional = false)
    @JoinColumn(name = "brand", nullable = false)
    var brand: Brand ?= null,
    var description: String? = null,
    @ElementCollection
    @CollectionTable(name = "product_keywords", joinColumns = [JoinColumn(name = "product_id")])
    var keywords: MutableSet<String> = HashSet(),
    @ManyToOne(optional = false)
    @JoinColumn(name = "point", nullable = false)
    var point: ProductPoint ?= null,
    @ManyToOne(optional = false)
    @JoinColumn(name = "price", nullable = false)
    var price: ProductPrice ?= null,
    @ManyToOne(optional = false)
    @JoinColumn(name = "stock", nullable = false)
    var stock: ProductStock ?= null,
    @ManyToOne(optional = false)
    @JoinColumn(name = "group", nullable = false)
    var group: Group ?= null,
    var disable: Boolean = true
)