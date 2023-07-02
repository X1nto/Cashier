package com.xinto.cashier.di

import com.xinto.cashier.network.registry.DefaultRegistryApi
import com.xinto.cashier.network.registry.RegistryApi
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val NetworkModule = module {
    singleOf(::DefaultRegistryApi) bind RegistryApi::class
}