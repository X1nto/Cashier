package dev.xinto.cashier.common

import android.app.Application
import dev.xinto.cashier.common.di.DatabaseModule
import dev.xinto.cashier.common.di.DomainModule
import dev.xinto.cashier.common.di.NetworkModule
import dev.xinto.cashier.common.di.UiModule
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