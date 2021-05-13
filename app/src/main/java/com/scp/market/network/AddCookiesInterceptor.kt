package com.scp.market.network

import okhttp3.Interceptor
import okhttp3.Response

class AddCookiesInterceptor(): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val builder = request.newBuilder()

        // TODO 액세스토큰을 가지고 있다면, 헤더에 다음 두 가지를 추가해 준다.
        // 1. 액세스 토큰
        // 2. 가맹점 식별 번호
        builder.addHeader("Content-Type", "application/json")
        builder.addHeader("access-token", "abc")
        builder.addHeader("ACCESS-AFFILIATE", "abc")

        request = builder.build()

        val response = chain.proceed(request)

        return response
    }
}