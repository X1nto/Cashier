package dev.xinto.cashier.common.di

import dev.xinto.cashier.common.network.registry.DefaultRegistryApi
import dev.xinto.cashier.common.network.registry.RegistryApi
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val NetworkModule = module {
    singleOf(::DefaultRegistryApi) bind RegistryApi::class
}