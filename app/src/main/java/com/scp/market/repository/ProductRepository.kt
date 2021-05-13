package com.scp.market.repository

import com.scp.market.api.ProductService
import com.scp.market.model.ProductInfo
import com.scp.market.model.product.request.ProductListRequest
import retrofit2.awaitResponse

class ProductRepository(private val productService: ProductService) {

    suspend fun registerProduct(productInfo: ProductInfo)
    = productService.registerProduct(productInfo).awaitResponse()
    suspend fun getProductList(productListRequest: ProductListRequest)
    =  productService.getProductList().awaitResponse()


}