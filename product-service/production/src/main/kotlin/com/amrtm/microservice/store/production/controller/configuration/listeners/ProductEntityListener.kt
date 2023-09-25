package com.amrtm.microservice.store.production.controller.configuration.listeners

interface ProductEntityListener<T> {
    fun loadPost(entity: T)
    fun updatePost(entity: T)
    fun deletePost(entity: T)
}