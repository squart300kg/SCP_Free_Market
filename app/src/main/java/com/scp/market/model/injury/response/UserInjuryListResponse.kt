package com.scp.market.model.injury.response


import com.google.gson.annotations.SerializedName

data class UserInjuryListResponse(
    val code: Int?,
    val `data`: Data?,
    val msg: String?,
    @SerializedName("request_count")
    val requestCount: Int?,
    val version: String?
)