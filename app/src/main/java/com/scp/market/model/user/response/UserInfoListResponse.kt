package com.scp.market.model.user.response


import com.google.gson.annotations.SerializedName

data class UserInfoListResponse(
    val code: Int?,
    val `data`: Data?,
    val msg: String?,
    @SerializedName("request_count")
    val requestCount: Int?,
    val version: String?
)