package com.scp.market.model.registerProduct.response

import com.scp.market.model.Category

data class CategoryListResponse(
         val msg: String?,
         val code: Int?,
         val request_count: Int?,
         val version: String,
         val data: List<Category>
)