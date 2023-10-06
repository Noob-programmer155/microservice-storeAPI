package com.amrtm.microservice.store.production.model.projection

import java.util.UUID

// using it for respository projection
interface Suggestion {
    val id: UUID
    val name: String
}