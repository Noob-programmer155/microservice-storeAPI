package com.amrtm.microservice.store.production.controller.repository.store_only


import com.amrtm.microservice.store.production.model.store_only.ProductPoint
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProductPointRepository: JpaRepository<ProductPoint, Long> {
    @Query("SELECT AVG(p.score) FROM product_point p WHERE p.product_id = ?1", nativeQuery = true)
    fun findAvg(id: UUID): Double
}