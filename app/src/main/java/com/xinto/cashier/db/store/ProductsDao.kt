package com.xinto.cashier.db.store

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.TypeConverters
import com.xinto.cashier.db.converter.ProductConverters
import com.xinto.cashier.db.entity.EntityPaymentType
import com.xinto.cashier.db.entity.EntityProduct
import com.xinto.cashier.db.entity.EntityProductType
import kotlinx.coroutines.flow.Flow

@Dao
@TypeConverters(ProductConverters::class)
interface ProductsDao {

    @Query("SELECT * FROM products")
    fun observeDailyProducts(): Flow<List<EntityProduct>>

    @Transaction
    suspend fun putDailyProducts(products: List<EntityProduct>) {
        products.forEach {
            putDailyProduct(
                id = it.id,
                name = it.name,
                price = it.price,
                quantity = it.quantity,
                payment = it.entityPaymentType,
                type = it.entityProductType
            )
        }
    }
    @Query("""
        INSERT OR REPLACE INTO products 
            (id, name, price, quantity, payment, type) 
        VALUES 
            (:id, :name, :price, COALESCE((SELECT quantity FROM products WHERE id = :id) + :quantity, :quantity), :payment, :type) 
    """)
    suspend fun putDailyProduct(id: String, name: String, price: Double, quantity: Int, payment: EntityPaymentType, type: EntityProductType)

    @Query("DELETE FROM products")
    suspend fun clear()

}