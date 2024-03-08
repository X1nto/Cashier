package dev.xinto.cashier.common.di

import dev.xinto.cashier.common.domain.repository.DailyStatusRepository
import dev.xinto.cashier.common.domain.repository.ProductsRepository
import dev.xinto.cashier.common.domain.repository.RegistryRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val DomainModule = module {
    singleOf(::DailyStatusRepository)
    singleOf(::RegistryRepository)
    singleOf(::ProductsRepository)
}