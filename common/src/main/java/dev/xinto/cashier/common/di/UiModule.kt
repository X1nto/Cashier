package dev.xinto.cashier.common.di

import dev.xinto.cashier.common.ui.screen.main.MainViewModel
import dev.xinto.cashier.common.ui.screen.registry.RegistryEditViewModel
import dev.xinto.cashier.common.ui.screen.registry.RegistryCashViewModel
import dev.xinto.cashier.common.ui.screen.registry.RegistryViewModel
import dev.xinto.cashier.common.ui.screen.income.IncomeViewModel
import dev.xinto.cashier.common.ui.screen.income.IncomeClearViewModel
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