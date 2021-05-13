package com.scp.market.api

import com.scp.market.model.ProductInfo
import com.scp.market.model.product.response.ProductListResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET

interface ProductService {

    @GET("shop/products")
    fun registerProduct(@Body productInfo: ProductInfo): Call<Void>

    @GET("shop/products")
    fun getProductList(): Call<ProductListResponse>

}