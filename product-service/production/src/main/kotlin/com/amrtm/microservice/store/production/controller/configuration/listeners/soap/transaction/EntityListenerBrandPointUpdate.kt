package com.amrtm.microservice.store.production.controller.configuration.listeners.soap.transaction

import com.amrtm.microservice.store.production.model.store_only.BrandPoint

data class EntityListenerBrandPointUpdate(
    var brandPoint: BrandPoint
)