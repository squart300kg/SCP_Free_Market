package com.scp.market.repository

import com.scp.market.api.RegisterService
import com.scp.market.model.ProductInfo
import retrofit2.awaitResponse

class RegisterRepository(private val registerService: RegisterService) {

    suspend fun registerProduct(productInfo: ProductInfo) = registerService.registerProduct(productInfo).awaitResponse()

}