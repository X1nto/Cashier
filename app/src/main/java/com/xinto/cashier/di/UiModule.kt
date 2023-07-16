package com.xinto.cashier.di

import com.xinto.cashier.ui.viewmodel.IncomeViewModel
import com.xinto.cashier.ui.viewmodel.MainViewModel
import com.xinto.cashier.ui.viewmodel.ProductViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val UiModule = module {
    viewModelOf(::IncomeViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::ProductViewModel)
}