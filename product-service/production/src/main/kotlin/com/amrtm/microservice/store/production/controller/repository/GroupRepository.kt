package com.amrtm.microservice.store.production.controller.repository

import com.amrtm.microservice.store.production.model.Group
import com.amrtm.microservice.store.production.model.Product
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface GroupRepository: PagingAndSortingRepository<Group,UUID> {}