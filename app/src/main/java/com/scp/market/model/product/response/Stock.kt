package com.scp.market.model.product.response


import com.google.gson.annotations.SerializedName

data class Stock(
    @SerializedName("stock_no_option")
    val stockNoOption: Int?,
    @SerializedName("stock_unlimit")
    val stockUnlimit: Boolean?,
    @SerializedName("stock_use")
    val stockUse: Boolean?
)