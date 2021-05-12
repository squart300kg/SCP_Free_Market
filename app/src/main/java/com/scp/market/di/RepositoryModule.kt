package com.scp.market.di

import com.scp.market.repository.LoginRepository
import com.scp.market.repository.RegisterRepository
import org.koin.dsl.module

val repositoryModule = module {

    single { RegisterRepository(get()) }
    single { LoginRepository(get()) }

}