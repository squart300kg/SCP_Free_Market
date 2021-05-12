package com.scp.market.repository

import com.scp.market.api.LoginService
import com.scp.market.model.login.request.AccessTokenRequest
import retrofit2.awaitResponse

class LoginRepository(private val loginService: LoginService) {

    suspend fun getAccessToekn(accessTokenRequest: AccessTokenRequest) = loginService.getAccessToken(accessTokenRequest).awaitResponse()

}