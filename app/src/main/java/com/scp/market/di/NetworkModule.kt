package com.scp.market.di

import com.scp.market.Application
import com.scp.market.BuildConfig
import com.scp.market.api.LoginService
import com.scp.market.api.RegisterService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.security.cert.CertificateException

/**
 * module - koin 모듈을 정의할 떄 사용
 * factory - inject하는 시점에 해당 객체를 생성
 * single - singletone
 * bind - 생성한 객체를 바인딩
 * get - 주입할 각 컴포넌트끼리의 의존성을 해결하기 위해 사용
 */

val networkModule = module {

    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            })
            .build()
    }

    single {
        Retrofit.Builder()
            .client(get())
            .baseUrl(Application.SERVICE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(RegisterService::class.java)
    }

    single {
        Retrofit.Builder()
            .client(get())
            .baseUrl(Application.SERVICE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(LoginService::class.java)
    }

}