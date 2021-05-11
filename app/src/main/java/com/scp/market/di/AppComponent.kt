package com.scp.market.di

import org.koin.dsl.module.Module

val appComponent = listOf(networkModule, viewModelModule, repositoryModule)