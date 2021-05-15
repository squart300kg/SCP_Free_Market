package com.scp.market.api

import com.scp.market.model.ProductInfo
import com.scp.market.model.product.response.ProductListResponse
import com.scp.market.model.registerProduct.request.RegisterProductRequest
import com.scp.market.model.registerProduct.response.CategoryListResponse
import com.scp.market.model.registerProduct.response.RegisterProductResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductService {

    @POST("shop/products")
    fun registerProduct(@Body registerProductRequest: RegisterProductRequest): Call<RegisterProductResponse>

    @GET("shop/products")
    fun getProductList(): Call<ProductListResponse>

    @GET("shop/categories")
    fun getCategories(): Call<CategoryListResponse>

}