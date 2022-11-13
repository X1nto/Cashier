package com.xinto.cashier.db.store

import android.util.Log
import com.xinto.cashier.db.entity.EntityProduct
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

interface ProductStore {

    fun observeDailyProducts(): Flow<List<EntityProduct>>

    suspend fun putDailyProducts(products: List<EntityProduct>)

}

object ProductStoreImpl : ProductStore {

    private val entities = mutableMapOf<String, EntityProduct>()
    private val flow = MutableSharedFlow<List<EntityProduct>>(replay = 1)

    override fun observeDailyProducts(): Flow<List<EntityProduct>> {
        return flow
    }

    override suspend fun putDailyProducts(products: List<EntityProduct>) {
        products.forEach {
            val existing = entities[it.id]
            if (existing != null) {
                entities[it.id] = existing.copy(
                    quantity = existing.quantity + it.quantity
                )
                Log.d("test", "price: ${existing.price}, ${it.price} quantity: ${existing.quantity}, ${it.quantity}")
            } else {
                entities[it.id] = it
            }
        }
        flow.emit(entities.values.toList())
    }

}