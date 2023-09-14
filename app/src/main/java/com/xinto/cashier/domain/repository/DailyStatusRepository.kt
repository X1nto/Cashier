package com.xinto.cashier.domain.repository

import com.xinto.cashier.db.entity.EntityPaymentType
import com.xinto.cashier.db.entity.EntityProduct
import com.xinto.cashier.db.entity.EntityProductType
import com.xinto.cashier.db.store.ProductsDao
import com.xinto.cashier.domain.model.BottleStatusProduct
import com.xinto.cashier.domain.model.MealStatusProduct
import com.xinto.cashier.domain.model.Price
import com.xinto.cashier.domain.model.StatusMode
import com.xinto.cashier.domain.model.StatusProduct
import com.xinto.cashier.domain.model.price
import com.xinto.cashier.domain.model.asPrice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DailyStatusRepository(
    private val productsDao: ProductsDao
) {

    fun observeProducts(statusMode: StatusMode): Flow<List<StatusProduct>> {
        return productsDao.observeDailyProducts().map { products ->
            products.filter {
                when (statusMode) {
                    StatusMode.CardMeal -> it.entityProductType == EntityProductType.Meal && it.entityPaymentType == EntityPaymentType.Card
                    StatusMode.CashMeal -> it.entityProductType == EntityProductType.Meal && it.entityPaymentType == EntityPaymentType.Cash
                    StatusMode.CardDrink -> it.entityProductType == EntityProductType.Bottle && it.entityPaymentType == EntityPaymentType.Card
                    StatusMode.CashDrink -> it.entityProductType == EntityProductType.Bottle && it.entityPaymentType == EntityPaymentType.Cash
                }
            }.map {
                it.toStatusProduct()
            }
        }
    }

    fun observeProducts(): Flow<List<StatusProduct>> {
        return productsDao.observeDailyProducts().map { products ->
            products.map {
                it.toStatusProduct()
            }
        }
    }

    suspend fun clear() {
        productsDao.clear()
    }

    private fun EntityProduct.toStatusProduct(): StatusProduct {
        return when (entityProductType) {
            EntityProductType.Meal -> {
                MealStatusProduct(
                    name = name,
                    mealPrice = price.asPrice(),
                    meals = quantity
                )
            }

            EntityProductType.Bottle -> {
                BottleStatusProduct(
                    name = name,
                    bottlePrice = price.asPrice(),
                    bottles = quantity
                )
            }

            else -> throw NotImplementedError()
        }
    }
}