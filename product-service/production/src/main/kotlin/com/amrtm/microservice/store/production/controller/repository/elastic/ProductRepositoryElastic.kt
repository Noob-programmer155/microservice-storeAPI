package com.amrtm.microservice.store.production.controller.repository.elastic

import co.elastic.clients.elasticsearch.core.GetResponse
import co.elastic.clients.elasticsearch.core.SearchResponse
import co.elastic.clients.elasticsearch.core.search.Suggestion
import com.amrtm.microservice.store.production.model.elastic.ProductElastic
import com.amrtm.microservice.store.production.model.filter.FilterProduct
import com.amrtm.microservice.store.production.model.filter.SearchProduct
import org.springframework.data.domain.Pageable
import java.util.UUID
import java.util.concurrent.CompletableFuture

interface ProductRepositoryElastic {
//    @Query("""
//        {
//            "term": {
//                "id": "?0"
//            }
//        }
//    """)
    fun getProduct(id: UUID): CompletableFuture<GetResponse<ProductElastic>>
//    @Query("""
//        {
//            "multi_match": {
//                "query": "?0",
//                "type": "bool_prefix",
//                "fields": [
//                    "name",
//                    "name._2gram",
//                    "name._3gram"
//                ]
//            }
//        }
//    """)
    fun getProductSearchSuggestion(search: String, page: Pageable): CompletableFuture<Map<String, List<Suggestion<ProductElastic>>>>
//    @Query("""
//        {
//            "bool": {
//                "should": [
//                    {"match": {"name": ":?#{#search.name}"}},
//                    {"match": {"brand.name": ":?#{#search.brand}"}},
//                    {"match": {"group.name": ":?#{#search.group}"}}
//                ]
//            },
//            "minimum_should_match" : 1,
//            "boost" : 1.0
//        }
//    """)
    fun getProductSearch(search: SearchProduct, page: Pageable): CompletableFuture<SearchResponse<ProductElastic>>
//    @Query("""
//        {
//            "bool": {
//                "filter": [
//                    {"range": {"price": { "gte": :?#{#filter.priceStart}, "lte": :?#{#filter.priceEnd} }}},
//                    {"range": {"score": { "gte": :?#{#filter.scoreStart}, "lte": :?#{#filter.scoreEnd} }}},
//                    {"range": {"brand.score": { "gte": :?#{#filter.brandScoreStart}, "lte": :?#{#filter.brandScoreEnd} }}},
//                    {"match": {"keywords": :?#{#filter.keywords} }},
//                    {"term": {"group.type": ":?#{#filter.groupType}"}}
//                ]
//            }
//        }
//    """)
    fun getProductFilter(filter: FilterProduct, page: Pageable): CompletableFuture<SearchResponse<ProductElastic>>
}