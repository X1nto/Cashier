package dev.xinto.cashier.common.di

import androidx.room.Room
import dev.xinto.cashier.common.db.CashierDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val DatabaseModule = module {
    single {
        Room.databaseBuilder(androidApplication(), CashierDatabase::class.java, "db")
            .build()
    }
    single {
        get<CashierDatabase>().getProductsDao()
    }
}