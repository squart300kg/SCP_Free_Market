package com.scp.market.api

import com.scp.market.model.login.request.AccessTokenRequest
import com.scp.market.model.login.response.AccessTokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {

    @POST("auth")
    fun getAccessToken(@Body accessTokenRequest: AccessTokenRequest): Call<AccessTokenResponse>

}