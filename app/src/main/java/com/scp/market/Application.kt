package com.scp.market

import android.app.Application
import android.util.Log
import com.scp.market.di.appComponent
import com.scp.market.model.User
import org.koin.android.ext.android.startKoin

open class Application: Application() {

    var user: String? = null

    override fun onCreate() {
        super.onCreate()

        instance = this

        configureDi()

        Log.i("ApplicationLog", "onCreate")

    }

    open fun configureDi() {
        Log.i("configureDiLog", "configureDi")
        startKoin(this, provideComponent())
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