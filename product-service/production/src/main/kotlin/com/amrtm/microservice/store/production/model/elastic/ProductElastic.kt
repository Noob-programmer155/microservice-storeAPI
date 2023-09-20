package com.amrtm.microservice.store.production.model.elastic

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import org.springframework.data.elasticsearch.annotations.InnerField
import org.springframework.data.elasticsearch.annotations.MultiField
import java.util.UUID

@Document(indexName = "product")
data class ProductElastic(
    @Id var id: UUID,
    @MultiField(mainField = Field(type = FieldType.Text), otherFields = [
        InnerField(suffix = "key",type = FieldType.Keyword),
        InnerField(suffix = "product",type = FieldType.Search_As_You_Type)
    ]) var name: String = "",
    @Field(type = FieldType.Object) var brand: BrandElastic ?= null,
    var keywords: MutableSet<String>,
    var score: UInt,
    var stock: Long,
    var price: Long,
    @MultiField(mainField = Field(type = FieldType.Text), otherFields = [
        InnerField(suffix = "hint", type = FieldType.Wildcard)
    ]) var description: String? = null,
    @Field(type = FieldType.Object) var group: GroupElastic ?= null,
    var disable: Boolean = true
)
