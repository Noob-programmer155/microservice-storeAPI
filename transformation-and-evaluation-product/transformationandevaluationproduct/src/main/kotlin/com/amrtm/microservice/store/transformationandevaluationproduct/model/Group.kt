package com.amrtm.microservice.store.transformationandevaluationproduct.model

import jakarta.xml.bind.annotation.XmlElement

data class Group(
    @get:XmlElement var id: Long?,
    @get:XmlElement var name: String = "",
    @get:XmlElement var type: String = "",
    @get:XmlElement var subgroups: MutableList<Group> = ArrayList(),
)