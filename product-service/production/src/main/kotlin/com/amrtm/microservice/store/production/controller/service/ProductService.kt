package com.amrtm.microservice.store.production.controller.service

import co.elastic.clients.elasticsearch.core.GetResponse
import co.elastic.clients.elasticsearch.core.SearchResponse
import co.elastic.clients.elasticsearch.core.search.Suggestion
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
import com.amrtm.microservice.store.production.model.store_only.BrandPoint
import com.amrtm.microservice.store.production.model.store_only.ProductPoint
import com.amrtm.microservice.store.production.model.store_only.ProductPrice
import com.amrtm.microservice.store.production.model.store_only.ProductStock
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.Date
import java.util.UUID
import java.util.concurrent.CompletableFuture

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
    @PersistenceContext var entityManager: EntityManager
) {
    // ElasticSearch
    fun get(id:UUID): CompletableFuture<GetResponse<ProductElastic>> {
        // create
        return productRepositoryElastic.getProduct(id)
    }
    fun getSuggestionSearch(pages: PageRequest, search: String):
            CompletableFuture<Map<String, List<Suggestion<ProductElastic>>>> {
        return productRepositoryElastic.getProductSearchSuggestion(search, pages)
    }
    fun getAllSearch(pages: PageRequest, search: SearchProduct): CompletableFuture<SearchResponse<ProductElastic>> {
        return productRepositoryElastic.getProductSearch(search, pages)
    }
    fun getAllFilter(pages: PageRequest, filter: FilterProduct): CompletableFuture<SearchResponse<ProductElastic>> {
        return productRepositoryElastic.getProductFilter(filter, pages)
    }
    // MySQL
    fun getAllGroup(page: PageRequest): Page<Group> {
        return groupRepository.findAll(page)
    }
    fun getAllBrand(page: PageRequest): Page<Brand> {
        return brandRepository.findAll(page)
    }
    @Transactional
    fun addScore(id: UUID, score: UInt) {
        val prod = ProductPoint(score = score.toFloat(), timestamp = Date().time)
        prod.addProduct(productRepository.findById(id).orElseThrow())
        productPointRepository.saveAndFlush(prod)
    }
    @Transactional
    fun addBrandScore(id: Long, score: UInt) {
        val brand = BrandPoint(score = score.toFloat(), timestamp = Date().time)
        brand.addBrand(brandRepository.findById(id).orElseThrow())
        brandPointRepository.saveAndFlush(brand)
    }
    @Transactional
    fun addStock(id: UUID, stock: Long) {
        val prod = ProductStock(stock = stock, timestamp = Date().time)
        prod.addProduct(productRepository.findById(id).orElseThrow())
        productStockRepository.saveAndFlush(prod)
    }
    @Transactional
    fun addPrice(id: UUID, price: Long) {
        val prod = ProductPrice(price = price, timestamp = Date().time)
        prod.addProduct(productRepository.findById(id).orElseThrow())
        productPriceRepository.saveAndFlush(prod)
    }
//    @Transactional
//    fun addSubgroup(mainGroup: Long,subgroup: Group) {
//        val gr = groupRepository.findById(mainGroup).orElseThrow()
//        gr.addSubgroup(if (subgroup.id != null) groupRepository.findById(subgroup.id!!).orElseThrow()
//        else groupRepository.saveAndFlush(subgroup))
//    }

    @Transactional
    fun store(data:Product): Product {
        val brandPoint:BrandPoint = if (data.brand?.score?.id != null) {
            val brP = brandPointRepository.findById(data.brand?.score?.id!!).orElseThrow()
            brP.brand_id = data.brand?.id ?: brP.brand_id
            brP.score = data.brand?.score?.score ?: brP.score
            brP
        } else data.brand?.score!!
        val brand:Brand = if (data.brand?.id != null) {
            val br = brandRepository.findById(data.brand?.id!!).orElseThrow()
            br.name = data.brand?.name ?: br.name
            br.score = data.brand?.score ?: br.score
            br
        } else {
            data.brand!!.score = null
            data.brand!!
        }

        val brandF = brandRepository.saveAndFlush(brand)
//        entityManager.refresh(brandF)
        brandPoint.brand_id = brandF.id
//        val brandPointF = brandPointRepository.saveAndFlush(brandPoint)
//        entityManager.refresh(brandPointF)
        brandPoint.addBrand(brand)
        brandF.addProduct(data)

        val group = if(data.group?.id != null) {
            val g = groupRepository.findById(data.group?.id!!).orElseThrow()
            g.name = data.group?.name ?: g.name
            g.type = data.group?.type ?: g.type
            g
        } else data.group!!

//        val groupF = groupRepository.saveAndFlush(group)
//        entityManager.refresh(groupF)
        group.addProduct(data)

        val productStock = if(data.stock?.id != null) {
            val ps = productStockRepository.findById(data.stock?.id!!).orElseThrow()
            ps.product_id = data.id ?: ps.product_id
            ps.stock = data.stock?.stock ?: ps.stock
            ps.timestamp = Date().time
            ps
        } else data.stock!!

//        val productStockF = productStockRepository.saveAndFlush(productStock)
//        entityManager.refresh(productStockF)

        val productPrice = if(data.price?.id != null) {
            val pp = productPriceRepository.findById(data.price?.id!!).orElseThrow()
            pp.product_id = data.id ?: pp.product_id
            pp.price = data.price?.price ?: pp.price
            pp.timestamp = Date().time
            pp
        } else data.price!!

//        val productPriceF = productPriceRepository.saveAndFlush(productPrice)
//        entityManager.refresh(productPriceF)

        val productPoint = if(data.point?.id != null) {
            val pp = productPointRepository.findById(data.point?.id!!).orElseThrow()
            pp.product_id = data.id ?: pp.product_id
            pp.score = data.point?.score ?: pp.score
            pp.timestamp = Date().time
            pp
        } else data.point!!

        data.point = null
        data.stock = null
        data.price = null
        val product = productRepository.saveAndFlush(data)
//        entityManager.refresh(product)

        productPoint.product_id = product.id
        productPoint.addProduct(product)
        productPointRepository.saveAndFlush(productPoint)

        productPrice.product_id = product.id
        productPrice.addProduct(data)
        productPriceRepository.saveAndFlush(productPrice)

        productStock.product_id = product.id
        productStock.addProduct(data)
        productStockRepository.saveAndFlush(productStock)

        return productRepository.findById(product.id!!).orElseThrow()
    }
    @Transactional
    fun delete(id:UUID) {
        productRepository.deleteById(id)
    }
    @Transactional
    fun deleteBrand(id:Long) {
        val br = brandRepository.findById(id)
        val brand = br.orElseThrow()
        brand.products?.forEach {
            it.brand = null
        }
        brandRepository.delete(brand)
    }
    @Transactional
    fun deleteGroup(id:Long) {
        val gr = groupRepository.findById(id)
        val group = gr.orElseThrow()
        group.products?.forEach {
            it.group = null
        }
        groupRepository.delete(group)
    }
}