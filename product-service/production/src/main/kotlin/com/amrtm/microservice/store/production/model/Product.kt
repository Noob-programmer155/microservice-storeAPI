package com.amrtm.microservice.store.production.model

import jakarta.persistence.*
import org.springframework.data.annotation.Id
import java.util.UUID

@Entity
@Table(name="product")
data class Product(
    @Id var id: UUID,
    @Column(unique = true) var name: String = "",
    @ManyToOne(optional = false)
    @JoinColumn(name = "Brand_Bridge", nullable = false) var brand: Brand ?= null,
    var description: String? = null,
    @ManyToOne(optional = false)
    @JoinColumn(name = "Group_Bridge", nullable = false) var group: Group ?= null,
    var disable: Boolean = true
)
