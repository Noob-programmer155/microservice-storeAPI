package com.amrtm.microservice.store.production.model.kafka

import java.util.Date

class KafkaMessage<K,V>(var key: K ?= null, var message: V ?= null, var timestamp : String = Date().time.toString())