package dev.xinto.cashier.common.domain.repository

import dev.xinto.cashier.common.db.entity.EntityPaymentType
import dev.xinto.cashier.common.db.entity.EntityProduct
import dev.xinto.cashier.common.db.entity.EntityProductType
import dev.xinto.cashier.common.db.store.ProductsDao
import dev.xinto.cashier.common.domain.model.BottleStatusProduct
import dev.xinto.cashier.common.domain.model.MealStatusProduct
import dev.xinto.cashier.common.domain.model.StatusMode
import dev.xinto.cashier.common.domain.model.StatusProduct
import dev.xinto.cashier.common.domain.model.asPrice
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