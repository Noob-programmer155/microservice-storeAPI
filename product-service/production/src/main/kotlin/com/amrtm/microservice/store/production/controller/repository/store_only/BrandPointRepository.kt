package com.amrtm.microservice.store.production.controller.repository.store_only


import com.amrtm.microservice.store.production.model.store_only.BrandPoint
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface BrandPointRepository: JpaRepository<BrandPoint, Long> {
    @Query("SELECT AVG(b.score) FROM brand_product_point b WHERE b.brand_id = ?1", nativeQuery = true)
    fun findAvg(id: Long): Double
}