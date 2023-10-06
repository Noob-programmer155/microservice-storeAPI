package com.amrtm.microservice.store.transformationandevaluationproduct.model.elastic


class GroupElastic(
//    @MultiField(mainField = Field(type = FieldType.Text), otherFields = [
//        InnerField(suffix = "key",type = FieldType.Keyword),
//        InnerField(suffix = "product",type = FieldType.Search_As_You_Type)
//    ])
    var name: String = "",
    var type: String? = null,
//    @Field(type = FieldType.Nested, includeInParent = true)
    var subgroups: MutableList<GroupElastic>
)