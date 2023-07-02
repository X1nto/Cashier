package com.xinto.cashier.di

import com.xinto.cashier.db.store.DefaultProductStore
import com.xinto.cashier.db.store.ProductStore
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val DatabaseModule = module {
    singleOf(::DefaultProductStore) bind ProductStore::class
}