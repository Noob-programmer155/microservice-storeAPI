package com.amrtm.microservice.store.production.model.projection

import java.util.UUID

interface Suggestion {
    val id: UUID
    val name: String
}