package com.scp.market.model.injury.response


import com.google.gson.annotations.SerializedName

data class Data(
    val list: List<Injury>?,
    val pagenation: Pagenation?
)