package com.scp.market.model.registerProduct.request

data class RegisterProductRequest(
        val categories: List<String?>,
        val images: List<String>?,
        val name: String?,
        val simple_content: String?,
        val content: String?,
        val price: Double?,
        val price_org: Double,
        val origin: String?
        )