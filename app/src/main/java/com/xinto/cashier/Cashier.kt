package com.xinto.cashier

import android.app.Application
import com.xinto.cashier.di.DatabaseModule
import com.xinto.cashier.di.DomainModule
import com.xinto.cashier.di.NetworkModule
import com.xinto.cashier.di.UiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Cashier : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@Cashier)

            modules(
                DatabaseModule,
                DomainModule,
                NetworkModule,
                UiModule
            )
        }
    }

}