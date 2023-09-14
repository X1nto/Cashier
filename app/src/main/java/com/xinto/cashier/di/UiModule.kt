package com.xinto.cashier.di

import com.xinto.cashier.ui.screen.main.MainViewModel
import com.xinto.cashier.ui.screen.registry.RegistryEditViewModel
import com.xinto.cashier.ui.screen.registry.RegistryCashViewModel
import com.xinto.cashier.ui.screen.registry.RegistryViewModel
import com.xinto.cashier.ui.screen.income.IncomeViewModel
import com.xinto.cashier.ui.screen.income.IncomeClearViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val UiModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::RegistryViewModel)
    viewModelOf(::RegistryEditViewModel)
    viewModelOf(::RegistryCashViewModel)
    viewModelOf(::IncomeViewModel)
    viewModelOf(::IncomeClearViewModel)
}