package com.xinto.cashier.di

import androidx.room.Room
import com.xinto.cashier.db.Database
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val DatabaseModule = module {
    single {
        Room.databaseBuilder(androidApplication(), Database::class.java, "db")
            .build()
    }
    single {
        get<Database>().getProductsDao()
    }
}