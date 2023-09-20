package com.amrtm.microservice.store.production.controller.service

import com.amrtm.microservice.store.production.controller.repository.ProductRepository
import com.amrtm.microservice.store.production.controller.repository.elastic.ProductRepositoryElastic
import com.amrtm.microservice.store.production.controller.repository.store_only.ProductPointRepository
import com.amrtm.microservice.store.production.controller.repository.store_only.ProductPriceRepository
import com.amrtm.microservice.store.production.controller.repository.store_only.ProductStockRepository
import com.amrtm.microservice.store.production.model.Product
import com.amrtm.microservice.store.production.model.elastic.ProductElastic
import com.amrtm.microservice.store.production.model.filter.FilterProduct
import com.amrtm.microservice.store.production.model.filter.SearchProduct
import com.amrtm.microservice.store.production.model.projection.Suggestion
import com.amrtm.microservice.store.production.model.store_only.ProductPoint
import com.amrtm.microservice.store.production.model.store_only.ProductPrice
import com.amrtm.microservice.store.production.model.store_only.ProductStock
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ProductService(
    var productRepository: ProductRepository,
    var productPointRepository: ProductPointRepository,
    var productStockRepository: ProductStockRepository,
    var productPriceRepository: ProductPriceRepository,
    var productRepositoryElastic: ProductRepositoryElastic,
) {
    // ElasticSearch
    fun get(id:UUID):ProductElastic {
        // create
        return productRepositoryElastic.getProduct(id)
    }
    fun getSuggestionSearch(pages: PageRequest, search: String): List<Suggestion> {
        return productRepositoryElastic.getProductSearchSuggestion(search, pages).content
    }
    fun getAllSearch(pages: PageRequest, search: SearchProduct): List<ProductElastic> {
        return productRepositoryElastic.getProductSearch(search, pages).content;
    }
    fun getAllFilter(pages: PageRequest, filter: FilterProduct): List<ProductElastic> {
        return productRepositoryElastic.getProductFilter(filter, pages).content;
    }
    // MySQL
    fun addScore(id: UUID, score: UInt) {
        productPointRepository.save(ProductPoint(product = id, score = score))
    }
    fun addStock(id: UUID, stock: Long) {
        productStockRepository.save(ProductStock(product = id, stock= stock))
    }
    fun addPrice(id: UUID, price: Long) {
        productPriceRepository.save(ProductPrice(product = id, price = price))
    }
    fun store(data:Product) {
        productRepository.save(data)
    }
    fun delete(id:UUID) {
        productRepository.deleteById(id)
    }
}