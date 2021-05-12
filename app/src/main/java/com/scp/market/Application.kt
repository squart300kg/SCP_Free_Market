package com.scp.market

import android.app.Application
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import com.scp.market.di.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

open class Application: Application() {

    var user: String? = null

    override fun onCreate() {
        super.onCreate()

        instance = this

        configureDi()

        initKakao()

        Log.i("ApplicationLog", "onCreate")

    }

    private fun initKakao() =
        KakaoSdk.init(this, "5bdefe59ab1fd149066e8f006b54c243")



    open fun configureDi() {
        Log.i("configureDiLog", "configureDi")
        startKoin {
            androidContext(this@Application)
            androidLogger()
            modules(provideComponent())
        }
    }

    open fun provideComponent() = appComponent

    companion object {
        private const val Tag = "Sauce Application"
        var instance: com.scp.market.Application? = null

        fun getGlobalApplicationContext(): com.scp.market.Application {
            checkNotNull(instance) { "this application does not inherit com.kakao.GlobalApplication" }
            return instance!!
        }

        var SERVICE_BASE_URL: String = BuildConfig.SERVICE_BASE_URL

    }

}