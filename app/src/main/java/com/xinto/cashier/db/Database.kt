package com.xinto.cashier.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xinto.cashier.db.entity.EntityProduct
import com.xinto.cashier.db.store.ProductsDao

@Database(entities = [EntityProduct::class], version = 1)
abstract class Database : RoomDatabase() {

    abstract fun getProductsDao(): ProductsDao

}