package com.scp.market

import androidx.core.content.res.TypedArrayUtils.getString
import com.scp.market.api.LoginService
import com.scp.market.model.login.request.AccessTokenRequest
import com.scp.market.ui.login.LoginViewModel
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class ModuleTest: KoinTest {

    val loginService: LoginService by inject<LoginService>()

    @Test
    fun moduleTest() {
    }
}