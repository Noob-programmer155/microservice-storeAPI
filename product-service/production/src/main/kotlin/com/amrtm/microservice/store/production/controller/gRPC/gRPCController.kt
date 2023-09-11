package com.amrtm.microservice.store.production.controller.gRPC

import com.amrtm.microservice.store.production.grpc.ProductAnalyzerGrpc
import com.amrtm.microservice.store.production.grpc.ProductAnalyzerGrpc.ProductAnalyzerFutureStub
import io.grpc.Channel

class gRPCController {
    val stub: ProductAnalyzerFutureStub
    constructor(channel: Channel) {
        stub = ProductAnalyzerGrpc.newFutureStub(channel)
    }


}