package com.amrtm.microservice.store.production.controller.service

import com.amrtm.microservice.store.production.controller.repository.BrandRepository
import com.amrtm.microservice.store.production.controller.repository.GroupRepository
import com.amrtm.microservice.store.production.controller.repository.ProductRepository
import com.amrtm.microservice.store.production.controller.repository.elastic.ProductRepositoryElastic
import com.amrtm.microservice.store.production.controller.repository.store_only.BrandPointRepository
import com.amrtm.microservice.store.production.controller.repository.store_only.ProductPointRepository
import com.amrtm.microservice.store.production.controller.repository.store_only.ProductPriceRepository
import com.amrtm.microservice.store.production.controller.repository.store_only.ProductStockRepository
import com.amrtm.microservice.store.production.model.Brand
import com.amrtm.microservice.store.production.model.Group
import com.amrtm.microservice.store.production.model.Product
import com.amrtm.microservice.store.production.model.elastic.ProductElastic
import com.amrtm.microservice.store.production.model.filter.FilterProduct
import com.amrtm.microservice.store.production.model.filter.SearchProduct
import com.amrtm.microservice.store.production.model.projection.Suggestion
import com.amrtm.microservice.store.production.model.store_only.BrandPoint
import com.amrtm.microservice.store.production.model.store_only.ProductPoint
import com.amrtm.microservice.store.production.model.store_only.ProductPrice
import com.amrtm.microservice.store.production.model.store_only.ProductStock
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ProductService(
    var groupRepository: GroupRepository,
    var brandRepository: BrandRepository,
    var brandPointRepository: BrandPointRepository,
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
    fun getSuggestionSearch(pages: PageRequest, search: String): Page<Suggestion> {
        return productRepositoryElastic.getProductSearchSuggestion(search, pages)
    }
    fun getAllSearch(pages: PageRequest, search: SearchProduct): Page<ProductElastic> {
        return productRepositoryElastic.getProductSearch(search, pages)
    }
    fun getAllFilter(pages: PageRequest, filter: FilterProduct): Page<ProductElastic> {
        return productRepositoryElastic.getProductFilter(filter, pages)
    }
    // MySQL
    fun getAllGroup(page: PageRequest): Page<Group> {
        return groupRepository.findAll(page)
    }
    fun getAllBrand(page: PageRequest): Page<Brand> {
        return brandRepository.findAll(page)
    }
    fun addScore(id: UUID, score: UInt) {
        val prod = ProductPoint(score = score)
        prod.product_id = id
        prod.addProduct(productRepository.findById(id).orElseThrow())
        productPointRepository.save(prod)
    }
    fun addBrandScore(id: Long, score: UInt) {
        val brand = BrandPoint(score = score)
        brand.brand_id = id
        brand.addBrand(brandRepository.findById(id).orElseThrow())
        brandPointRepository.save(brand)
    }
    fun addStock(id: UUID, stock: Long) {
        val prod = ProductStock(stock = stock)
        prod.product_id = id
        prod.addProduct(productRepository.findById(id).orElseThrow())
        productStockRepository.save(prod)
    }
    fun addPrice(id: UUID, price: Long) {
        val prod = ProductPrice(price = price)
        prod.product_id = id
        prod.addProduct(productRepository.findById(id).orElseThrow())
        productPriceRepository.save(prod)
    }
    fun store(data:Product) {
        data.brand?.addProduct(data)
        data.brand?.score?.addBrand(data.brand!!)
        data.brand?.score?.brand_id = data.brand?.id

        data.price?.product_id = data.id
        data.price?.addProduct(data)

        data.point?.product_id = data.id
        data.point?.addProduct(data)

        data.stock?.product_id = data.id
        data.stock?.addProduct(data)

        data.group?.subgroups?.forEach {
            it.addProduct(data)
        }
        data.group?.addProduct(data)
        productRepository.save(data)
    }
    fun delete(id:UUID) {
        productRepository.deleteById(id)
    }
    fun deleteBrand(id:Long) {
        val br = brandRepository.findById(id)
        val brand = br.orElseThrow()
        brand.products.forEach {
            it.brand = null
        }
        brandRepository.delete(brand)
    }
    fun deleteGroup(id:Long) {
        val gr = groupRepository.findById(id)
        val group = gr.orElseThrow()
        group.products.forEach {
            it.group = null
        }
        group.groups.forEach {
            group.removeSubgroup(it)
            it.removeSubgroup(group)
        }
        groupRepository.delete(group)
    }
}