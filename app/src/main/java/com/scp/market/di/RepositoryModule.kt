package com.scp.market.di

import com.scp.market.repository.RegisterRepository
import org.koin.dsl.module.module

val repositoryModule = module {
    factory { RegisterRepository(get()) }
}