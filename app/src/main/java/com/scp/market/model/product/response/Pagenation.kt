package com.scp.market.model.product.response


import com.google.gson.annotations.SerializedName

data class Pagenation(
    @SerializedName("current_page")
    val currentPage: Int?,
    @SerializedName("data_count")
    val dataCount: String?,
    val pagesize: Int?,
    @SerializedName("total_page")
    val totalPage: Int?
)