package com.amrtm.microservice.store.production.controller.repository.elastic

import com.amrtm.microservice.store.production.model.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.annotations.Query
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ProductRepositoryElastic: ElasticsearchRepository<Product, UUID> {
    @Query("""
        {
            "match": {
                "id": "?0"
            }
        }
    """)
    fun getProduct(id: UUID): Product
    @Query("""
        {
            "bool": {
                "filter": {
                    "term": {"name":":?#{#product.name}"},
                }
            }
        }
    """)
    fun getProduct(filter: Product, page: Pageable): Page<Product>
}