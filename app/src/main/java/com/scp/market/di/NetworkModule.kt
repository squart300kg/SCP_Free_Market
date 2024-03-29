package com.scp.market.di

import com.scp.market.Application
import com.scp.market.BuildConfig
import com.scp.market.api.LoginService
import com.scp.market.api.MyRoomService
import com.scp.market.api.ProductService
import com.scp.market.network.AddCookiesInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * module - koin 모듈을 정의할 떄 사용
 * factory - inject하는 시점에 해당 객체를 생성
 * single - singletone
 * bind - 생성한 객체를 바인딩
 * get - 주입할 각 컴포넌트끼리의 의존성을 해결하기 위해 사용
 */

val networkModule = module {

    // TODO 1. single패턴을 factory패턴으로 바꿀 것
    //  2. qualifier을 지정할 것,

    factory<Interceptor> {
        AddCookiesInterceptor(get())
    }

    factory {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            })
            .addInterceptor(get() as Interceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    single {
        Retrofit.Builder()
            .client(get())
            .baseUrl(Application.SERVICE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    factory { get<Retrofit>().create(ProductService::class.java) }
    factory { get<Retrofit>().create(LoginService::class.java) }
    factory { get<Retrofit>().create(MyRoomService::class.java) }

}