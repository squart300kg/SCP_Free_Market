package com.scp.market.repository

import com.scp.market.api.ProductService
import com.scp.market.model.ProductInfo
import com.scp.market.model.product.request.ProductListRequest
import com.scp.market.model.registerProduct.request.RegisterProductRequest
import retrofit2.awaitResponse

class ProductRepository(private val productService: ProductService) {

    suspend fun registerProduct(registerProductRequest: RegisterProductRequest)
    = productService.registerProduct(registerProductRequest).awaitResponse()
    suspend fun getProductList(productListRequest: ProductListRequest)
    =  productService.getProductList().awaitResponse()

    suspend fun getCategories()
    = productService.getCategories().awaitResponse()


}