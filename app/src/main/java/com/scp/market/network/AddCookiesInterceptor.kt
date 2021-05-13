package com.scp.market.network

import android.util.Log
import com.securepreferences.SecurePreferences
import okhttp3.Interceptor
import okhttp3.Response

class AddCookiesInterceptor(private val securePreferences: SecurePreferences): Interceptor {

    val TAG = "AddCookiesInterceptor"

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val builder = request.newBuilder()

        val token = securePreferences.getEncryptedString("token", "")

        if (token != null && token.isNotEmpty()) {
            builder.addHeader("access-token", token)
        }

        builder.addHeader("Content-Type", "application/json")
        builder.addHeader("Accept", "application/json")

        request = builder.build()

        val response = chain.proceed(request)

        return response
    }
}