package com.scp.market.model.product.response


import com.google.gson.annotations.SerializedName

data class Badge(
    val best: Boolean?,
    val hot: Boolean?,
    val md: Boolean?,
    val new: Boolean?
)