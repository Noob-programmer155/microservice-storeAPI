package com.amrtm.microservice.store.production.model.elastic

data class ProductElastic(
//    @MultiField(mainField = Field(type = FieldType.Text), otherFields = [
//        InnerField(suffix = "key",type = FieldType.Keyword),
//        InnerField(suffix = "product",type = FieldType.Search_As_You_Type)
//    ])
    var name: String = "",
//    @Field(type = FieldType.Object)
    var brand: BrandElastic ?= null,
    var keywords: MutableSet<String>,
    var score: Float,
    var stock: Long,
    var price: Long,
//    @MultiField(mainField = Field(type = FieldType.Text), otherFields = [
//        InnerField(suffix = "hint", type = FieldType.Wildcard)
//    ])
    var description: String? = null,
//    @Field(type = FieldType.Object)
    var group: GroupElastic ?= null,
    var disable: Boolean = true
)
