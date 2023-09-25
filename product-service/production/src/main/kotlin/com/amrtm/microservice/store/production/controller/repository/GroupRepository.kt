package com.amrtm.microservice.store.production.controller.repository

import com.amrtm.microservice.store.production.model.Group
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface GroupRepository: JpaRepository<Group, Long>, PagingAndSortingRepository<Group, Long>