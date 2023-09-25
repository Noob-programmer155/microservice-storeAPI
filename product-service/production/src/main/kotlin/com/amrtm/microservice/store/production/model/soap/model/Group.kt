package com.amrtm.microservice.store.production.model.soap.model

import com.amrtm.microservice.store.production.controller.configuration.listeners.GroupEntityListener
import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlTransient

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator::class)
@EntityListeners(GroupEntityListener::class)
@Table(name="group")
data class Group(
    @Id @GeneratedValue
    @get:XmlElement var id: Long?,
    @Column(unique = true)
    @get:XmlElement var name: String = "",
    @get:XmlElement var type: String = "",
    @ManyToMany(mappedBy = "groups", fetch = FetchType.LAZY)
    @get:XmlElement var subgroups: MutableList<Group> = ArrayList(),
    @JsonIgnore
    @XmlTransient
    @ManyToMany(cascade = [CascadeType.MERGE], fetch = FetchType.LAZY)
    @JoinTable(
        name = "Self_Proj", joinColumns = [JoinColumn(name = "Group_Owner", nullable = false)],
        inverseJoinColumns = [JoinColumn(name = "Group_Rel", nullable = false)]
    ) var groups: MutableList<Group> = ArrayList(),
    @JsonIgnore
    @XmlTransient
    @OneToMany(
        mappedBy = "group",
        cascade = [CascadeType.MERGE, CascadeType.PERSIST]
    ) var products: MutableSet<Product> = HashSet()
) {
    fun addSubgroup(group: Group) {
        if (this.subgroups.contains(group)) return
        this.subgroups.add(group)
        this.groups.add(this)
    }
    fun removeSubgroup(group: Group) {
        if (!this.subgroups.contains(group)) return
        this.subgroups.remove(group)
        this.groups.remove(this)
    }

    fun addProduct(product: Product) {
        this.products.add(product)
        product.group = this
    }
    fun removeProduct(product: Product) {
        this.products.remove(product)
        product.group = null
    }
}