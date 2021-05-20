package com.scp.market.model.user.response


import com.google.gson.annotations.SerializedName

data class Data(
    val list: List<UserInfo>?,
    val pagenation: Pagenation?
)