package com.scp.market.di

import com.scp.market.Application
import com.scp.market.api.RegisterService
import okhttp3.OkHttpClient
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

    factory {
        val builder = OkHttpClient.Builder()

        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }

                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                }
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory

            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier(HostnameVerifier { _, _ ->
                true
            })
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

        builder.connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)

    }

//    single<Retrofit>("register") {
//        Retrofit.Builder()
//            .client(get())
//            .baseUrl(Application.SERVICE_BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .addConverterFactory(MoshiConverterFactory.create())
//            .build()
//    }
//
//    factory { get<Retrofit>("register").create(RegisterService::class.java) }

}