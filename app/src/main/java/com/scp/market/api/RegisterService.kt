package com.scp.market.api

import com.scp.market.model.ProductInfo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET

interface RegisterService {

    @GET("hello/hello")
    fun registerProduct(@Body productInfo: ProductInfo): Call<Void>

}