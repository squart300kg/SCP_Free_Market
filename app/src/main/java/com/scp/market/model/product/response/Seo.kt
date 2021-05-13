package com.scp.market.model.product.response


import com.google.gson.annotations.SerializedName

data class Seo(
    @SerializedName("access_bot")
    val accessBot: Boolean?,
    val desc: Desc?,
    val title: Title?
)