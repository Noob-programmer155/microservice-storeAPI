package com.amrtm.microservice.store.production.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Getter
import lombok.Setter
import java.math.BigInteger
import java.util.UUID

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
class Product {
    @Id
    @GeneratedValue
    val id: UUID ?= null
    lateinit var name: String
    @ManyToOne(optional = false)
    @JoinColumn(name="Brand_Bridge", nullable = false)
    lateinit var brands: Brand
    val score: Int = 0
    val stock: Long = 0
    lateinit var price: BigInteger
    lateinit var description: String
    @ManyToOne(optional = false)
    @JoinColumn(name="Group_Bridge", nullable = false)
    lateinit var groups: Group
    val disable: Boolean = false
}
