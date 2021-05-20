package com.scp.market.di

import com.scp.market.repository.LoginRepository
import com.scp.market.repository.MyRoomRepository
import com.scp.market.repository.ProductRepository
import com.securepreferences.SecurePreferences
import org.koin.dsl.module

val repositoryModule = module {

    single { SecurePreferences(get(), "", "my_prefs.xml") }
    single { ProductRepository(get()) }
    single { LoginRepository(get()) }
    single { MyRoomRepository(get()) }

}