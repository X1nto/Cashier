package com.xinto.cashier.di

import com.xinto.cashier.domain.repository.DailyStatusRepository
import com.xinto.cashier.domain.repository.RegistryRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val DomainModule = module {
    singleOf(::DailyStatusRepository)
    singleOf(::RegistryRepository)
}