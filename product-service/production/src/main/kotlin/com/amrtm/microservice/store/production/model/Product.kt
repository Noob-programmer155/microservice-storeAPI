package com.amrtm.microservice.store.production.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigInteger
import java.util.UUID

@Entity
@Table(name="product")
class Product {
    @Id
    @GeneratedValue
    var id: UUID ?= null
    @Column(unique = true)
    var name: String
    @ManyToOne(optional = false)
    @JoinColumn(name="Brand_Bridge", nullable = false)
    var brand: Brand ?= null
    var score: Int = 0
    var stock: Long = 0
    var price: BigInteger
    var description: String
    @ManyToOne(optional = false)
    @JoinColumn(name="Group_Bridge", nullable = false)
    var group: Group ?= null
    var disable: Boolean = false

    constructor(id: UUID?, name: String, brand: Brand?, score: Int, stock: Long, price: BigInteger, description: String,
                group: Group?, disable: Boolean) {
        this.id = id
        this.name = name
        this.brand = brand
        this.score = score
        this.stock = stock
        this.price = price
        this.description = description
        this.group = group
        this.disable = disable
    }
}
