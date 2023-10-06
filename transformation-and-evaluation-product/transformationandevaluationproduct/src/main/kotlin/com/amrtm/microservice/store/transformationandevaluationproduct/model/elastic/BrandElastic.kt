package com.amrtm.microservice.store.transformationandevaluationproduct.model.elastic

data class BrandElastic(
//    @MultiField(mainField = Field(type = FieldType.Text), otherFields = [
//        InnerField(suffix = "key",type = FieldType.Keyword),
//        InnerField(suffix = "product",type = FieldType.Search_As_You_Type)
//    ])
    var name: String = "",
    var score: Float
)