package com.amrtm.microservice.store.production.controller.repository.elastic

import com.amrtm.microservice.store.production.model.elastic.ProductElastic
import com.amrtm.microservice.store.production.model.filter.FilterProduct
import com.amrtm.microservice.store.production.model.filter.SearchProduct
import com.amrtm.microservice.store.production.model.projection.Suggestion
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.annotations.Query
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ProductRepositoryElastic: ElasticsearchRepository<ProductElastic, UUID> {
    @Query("""
        {
            "term": {
                "id": "?0"
            }
        }
    """)
    fun getProduct(id: UUID): ProductElastic
    @Query("""
        {
            "multi_match": {
                "query": "?0",
                "type": "bool_prefix",
                "fields": [
                    "name",
                    "name._2gram",
                    "name._3gram"
                ]
            }
        }
    """)
    fun getProductSearchSuggestion(search: String, page: Pageable): Page<Suggestion>
    @Query("""
        {
            "bool": {
                "should": [
                    {"match": {"name": ":?#{#search.name}"}},
                    {"match": {"brand.name": ":?#{#search.brand}"}},
                    {"match": {"group.name": ":?#{#search.group}"}}
                ]
            },
            "minimum_should_match" : 1,
            "boost" : 1.0
        }
    """)
    fun getProductSearch(search: SearchProduct, page: Pageable): Page<ProductElastic>
    @Query("""
        {
            "bool": {
                "filter": [
                    {"range": {"price": { "gte": :?#{#filter.priceStart}, "lte": :?#{#filter.priceEnd} }}},
                    {"range": {"score": { "gte": :?#{#filter.scoreStart}, "lte": :?#{#filter.scoreEnd} }}},
                    {"range": {"brand.score": { "gte": :?#{#filter.brandScoreStart}, "lte": :?#{#filter.brandScoreEnd} }}},
                    {"match": {"keywords": :?#{#filter.keywords} }},
                    {"term": {"group.type": ":?#{#filter.groupType}"}}
                ]
            }
        }
    """)
    fun getProductFilter(filter: FilterProduct, page: Pageable): Page<ProductElastic>
}