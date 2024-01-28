package dev.xinto.cashier.common.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.xinto.cashier.common.db.entity.EntityProduct
import dev.xinto.cashier.common.db.store.ProductsDao

@Database(entities = [EntityProduct::class], version = 1)
abstract class CashierDatabase : RoomDatabase() {

    abstract fun getProductsDao(): ProductsDao

}