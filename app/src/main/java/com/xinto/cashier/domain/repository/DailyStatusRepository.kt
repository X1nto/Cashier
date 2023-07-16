package com.xinto.cashier.domain.repository

import com.xinto.cashier.db.entity.EntityPaymentType
import com.xinto.cashier.db.entity.EntityProduct
import com.xinto.cashier.db.entity.EntityProductType
import com.xinto.cashier.db.store.ProductsDao
import com.xinto.cashier.domain.model.BottleStatusProduct
import com.xinto.cashier.domain.model.MealStatusProduct
import com.xinto.cashier.domain.model.StatusProduct
import com.xinto.cashier.domain.model.toPrice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DailyStatusRepository(
    private val productsDao: ProductsDao
) {

    fun observeCardDrinks(): Flow<List<StatusProduct>> {
        return observeProduct {
            it.entityProductType == EntityProductType.Bottle && it.entityPaymentType == EntityPaymentType.Card
        }
    }

    fun observeCashDrinks(): Flow<List<StatusProduct>> {
        return observeProduct {
            it.entityProductType == EntityProductType.Bottle && it.entityPaymentType == EntityPaymentType.Cash
        }
    }

    fun observeCardMeals(): Flow<List<StatusProduct>> {
        return observeProduct {
            it.entityProductType == EntityProductType.Meal && it.entityPaymentType == EntityPaymentType.Card
        }
    }

    fun observeCashMeals(): Flow<List<StatusProduct>> {
        return observeProduct {
            it.entityProductType == EntityProductType.Meal && it.entityPaymentType == EntityPaymentType.Cash
        }
    }

    suspend fun clear() {
        productsDao.clear()
    }

    private inline fun observeProduct(
        crossinline filter: (EntityProduct) -> Boolean
    ): Flow<List<StatusProduct>> {
        return productsDao.observeDailyProducts().map { products ->
            products.filter(filter).map {
                it.toStatusProduct()
            }
        }
    }

    private fun EntityProduct.toStatusProduct(): StatusProduct {
        return when (entityProductType) {
            EntityProductType.Meal -> {
                MealStatusProduct(
                    name = name,
                    mealPrice = price.toPrice(),
                    meals = quantity
                )
            }
            EntityProductType.Bottle -> {
                BottleStatusProduct(
                    name = name,
                    bottlePrice = price.toPrice(),
                    bottles = quantity
                )
            }
            else -> throw NotImplementedError()
        }
    }

}