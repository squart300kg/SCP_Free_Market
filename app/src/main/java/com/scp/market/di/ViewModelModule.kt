package com.scp.market.di

import com.scp.market.ui.login.LoginViewModel
import com.scp.market.ui.register.RegisterViewModel
import com.scp.market.ui.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { LoginViewModel(get(), get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { SearchViewModel(get()) }

}