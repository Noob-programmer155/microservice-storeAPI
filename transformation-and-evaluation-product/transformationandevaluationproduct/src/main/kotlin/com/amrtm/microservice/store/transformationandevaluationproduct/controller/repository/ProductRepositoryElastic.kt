package com.amrtm.microservice.store.transformationandevaluationproduct.controller.repository

import com.amrtm.microservice.store.transformationandevaluationproduct.model.elastic.ProductElastic

interface ProductRepositoryElastic<T,ID> {
    fun save(product:T, id:ID)
    fun delete(id:ID)
}