package com.amrtm.microservice.store.production.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Getter
import lombok.Setter

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
class Brand {
    @Id
    @GeneratedValue
    val id: Long ?= null
    lateinit var name: String
    val score: UInt = 1u
    @JsonIgnore
    @OneToMany(mappedBy = "brands", cascade = [CascadeType.MERGE, CascadeType.PERSIST])
    lateinit var products: List<Product>
}