package com.amrtm.microservice.store.production.model

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Getter
import lombok.Setter

@Entity
@Builder
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator::class)
class Group {
    @Id
    @GeneratedValue
    @Getter @Setter var id: Long ?= null
    @Column(unique = true)
    @Getter @Setter lateinit var name: String
    @Getter @Setter lateinit var type: String
    @ManyToMany(mappedBy = "groups", fetch = FetchType.LAZY)
    var subgroups: MutableList<Group> = ArrayList()
    @JsonIgnore
    @ManyToMany(cascade = [CascadeType.MERGE], fetch = FetchType.LAZY)
    @JoinTable(name="Self_Proj", joinColumns = [JoinColumn(name="Group_Owner", nullable = false)],
            inverseJoinColumns = [JoinColumn(name="Group_Rel", nullable = false)])
    var groups: MutableList<Group> = ArrayList()
    @JsonIgnore
    @OneToMany(mappedBy = "group", cascade = [CascadeType.MERGE,CascadeType.PERSIST])
    var products: MutableList<Product> = ArrayList()

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