package com.amrtm.microservice.store.production.model.soap

import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "response")
data class SOAPResponseGlobal(
    @get:XmlElement var message: String,
    @get:XmlElement var err: Boolean
)