package com.xinto.cashier.db.store

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.TypeConverters
import com.xinto.cashier.db.converter.ProductConverters
import com.xinto.cashier.db.entity.EntityProduct
import kotlinx.coroutines.flow.Flow

@Dao
@TypeConverters(ProductConverters::class)
interface ProductsDao {

    @Query("SELECT * FROM products")
    fun observeDailyProducts(): Flow<List<EntityProduct>>

    @Insert
    suspend fun putDailyProducts(products: List<EntityProduct>)

    @Query("DELETE FROM products")
    suspend fun clear()

}