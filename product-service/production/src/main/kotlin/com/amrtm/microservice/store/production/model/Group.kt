package com.amrtm.microservice.store.production.model

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Getter
import lombok.Setter

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator::class)
class Group {
    @Id
    @GeneratedValue
    val id: Long ?= null
    lateinit var name: String
    lateinit var type: String
    @OneToMany(mappedBy = "group", cascade= [CascadeType.MERGE,CascadeType.PERSIST])
    lateinit var subgroup: List<Group>
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="self_proj")
    lateinit var group: Group
    @JsonIgnore
    @OneToMany(mappedBy = "groups", cascade = [CascadeType.MERGE,CascadeType.PERSIST])
    lateinit var products: List<Product>
}