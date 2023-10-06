package com.amrtm.microservice.store.production.model

import com.amrtm.microservice.store.production.controller.configuration.listeners.GroupEntityListener
import com.fasterxml.jackson.annotation.*
import jakarta.persistence.*
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlTransient

@Entity
@Table(name="group_product")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "id")
@EntityListeners(GroupEntityListener::class)
@JsonIgnoreProperties(ignoreUnknown = true)
class Group(
    @Id @GeneratedValue
    @get:XmlElement var id: Long? = null,
    @Column(unique = true)
    @get:XmlElement var name: String = "",
    @Column(name="group_type")
    @get:XmlElement var type: String = "",
    @OneToMany(mappedBy = "group", cascade = [CascadeType.MERGE])
    @get:XmlElement var subgroups: MutableSet<Group> ?= null,
    @JsonIgnore
    @XmlTransient
    @ManyToOne
    @JoinColumn(name = "owner_group", nullable = true)
    var group: Group ?= null,
    @JsonIgnore
    @XmlTransient
    @OneToMany(mappedBy = "group", cascade = [CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH]) var products: MutableSet<Product> ?= null
) {
    fun addSubgroup(group: Group) {
        if (this.subgroups == null) this.subgroups = mutableSetOf()
        if (this.subgroups?.contains(group)!!) return
        this.subgroups?.add(group)
        this.group = this
    }
    fun removeSubgroup(group: Group) {
        if (this.subgroups != null && !this.subgroups?.contains(group)!!) return
        this.subgroups?.remove(group)
        this.group = this
    }

    fun addProduct(product: Product) {
        if (this.products == null) this.products = mutableSetOf()
        if (this.products?.contains(product)!!) return
        this.products?.add(product)
        product.group = this
    }
    fun removeProduct(product: Product) {
        if (this.products != null && !this.products?.contains(product)!!) return
        this.products?.remove(product)
        product.group = null
    }
}