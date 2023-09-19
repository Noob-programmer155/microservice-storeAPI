package com.amrtm.microservice.store.production.controller.repository.store_only

import com.amrtm.microservice.store.production.model.store_only.ProductStock
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductStockRepository: JpaRepository<ProductStock, Long> {}