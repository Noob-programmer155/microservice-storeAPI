package com.amrtm.microservice.store.production.controller.configuration.listeners.soap.transaction

import com.amrtm.microservice.store.production.model.store_only.ProductPoint

data class EntityListenerProductPointUpdate(
    var productPoint: ProductPoint
)