package com.scp.market.model.login.response

data class AccessTokenResponse(
    val mes: String,
    val code: Int,
    val access_token: String)
