package com.scp.market.model.user.response


import com.google.gson.annotations.SerializedName

data class UserInfo(
    val callnum: String?,
    val email: String?,
    val name: String?,
    val uid: String?
)