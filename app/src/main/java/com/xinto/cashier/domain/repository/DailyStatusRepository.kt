package com.xinto.cashier.domain.repository

import com.xinto.cashier.db.entity.EntityProduct
import com.xinto.cashier.db.entity.EntityProductType
import com.xinto.cashier.db.store.ProductStoreImpl
import com.xinto.cashier.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface DailyStatusRepository {

    fun observeCashDrinks(): Flow<List<StatusProduct>>
    fun observeCardDrinks(): Flow<List<StatusProduct>>
    
    fun observeCashMeals(): Flow<List<StatusProduct>>
    fun observeCardMeals(): Flow<List<StatusProduct>>

}

object DailyStatusRepositoryImpl : DailyStatusRepository {

    override fun observeCardDrinks(): Flow<List<StatusProduct>> {
        return observeProduct { it.isBottle && it.isPaidCard }
    }

    override fun observeCashDrinks(): Flow<List<StatusProduct>> {
        return observeProduct { it.isBottle && it.isPaidCash }
    }

    override fun observeCardMeals(): Flow<List<StatusProduct>> {
        return observeProduct { it.isMeal && it.isPaidCard }
    }

    override fun observeCashMeals(): Flow<List<StatusProduct>> {
        return observeProduct { it.isMeal && it.isPaidCash }
    }

    private inline fun observeProduct(
        crossinline filter: (EntityProduct) -> Boolean
    ): Flow<List<StatusProduct>> {
        return ProductStoreImpl.observeDailyProducts().map { products ->
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