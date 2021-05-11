package com.scp.market.di

import com.scp.market.ui.register.RegisterViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {
    viewModel { RegisterViewModel(get()) }
}