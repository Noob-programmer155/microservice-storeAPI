package com.amrtm.microservice.store.production.controller.repository

import com.amrtm.microservice.store.production.model.Product
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ProductRepository: PagingAndSortingRepository<Product,UUID> {}