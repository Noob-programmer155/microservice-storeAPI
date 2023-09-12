package com.amrtm.microservice.store.production.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
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
@Table(name="product")
class Product {
    @Id
    @GeneratedValue
    var id: UUID ?= null
    @Column(unique = true)
    lateinit var name: String
    @ManyToOne(optional = false)
    @JoinColumn(name="Brand_Bridge", nullable = false)
    var brand: Brand ?= null
    var score: Int = 0
    var stock: Long = 0
    lateinit var price: BigInteger
    lateinit var description: String
    @ManyToOne(optional = false)
    @JoinColumn(name="Group_Bridge", nullable = false)
    var group: Group ?= null
    var disable: Boolean = false
}
