package com.xinto.cashier.domain.repository

import com.xinto.cashier.db.entity.EntityPaymentType
import com.xinto.cashier.db.entity.EntityProduct
import com.xinto.cashier.db.entity.EntityProductType
import com.xinto.cashier.db.store.ProductStore
import com.xinto.cashier.domain.model.*
import com.xinto.cashier.domain.model.Result
import com.xinto.cashier.network.registry.RegistryApi
import com.xinto.cashier.network.registry.model.ApiProductType


class RegistryRepository(
    private val registryApi: RegistryApi,
    private val productStore: ProductStore
) {

    suspend fun getProducts(): Result<List<SelectableProduct>> {
        return try {
            val data = registryApi.parseProducts().map {
                when (it.type) {
                    ApiProductType.Bottle -> {
                        BottleSelectableProduct(
                            name = it.name,
                            price = it.price.toDouble()
                        )
                    }
                    ApiProductType.Meal -> {
                        MealSelectableProduct(
                            name = it.name,
                            price = it.price.toDouble()
                        )
                    }
                }
            }
            Result.Success(data)
        } catch (e: Exception) {
            Result.Error
        }
    }

    suspend fun payWithCard(selectedProducts: List<SelectedProduct>) {
        storeProducts(selectedProducts, EntityPaymentType.Card)
    }

    suspend fun payWithCash(selectedProducts: List<SelectedProduct>) {
        storeProducts(selectedProducts, EntityPaymentType.Cash)
    }

    private suspend fun storeProducts(
        selectedProducts: List<SelectedProduct>,
        entityPaymentType: EntityPaymentType
    ) {
        productStore.putDailyProducts(
            selectedProducts.map {
                it.toEntityProduct(entityPaymentType)
            }
        )
    }

    private fun SelectedProduct.toEntityProduct(entityPaymentType: EntityPaymentType): EntityProduct {
        return EntityProduct(
            id = name + entityPaymentType.toString(),
            name = name,
            price = when (this) {
                is BottleSelectedProduct -> bottlePrice
                is MealSelectedProduct -> mealPrice
                is MeasuredSelectedProduct -> pricePerKilo
            }.price,
            quantity = when (this) {
                is BottleSelectedProduct -> bottles
                is MealSelectedProduct -> meals
                is MeasuredSelectedProduct -> kilos.toInt()
            },
            entityPaymentType = entityPaymentType,
            entityProductType = when (this) {
                is BottleSelectedProduct -> EntityProductType.Bottle
                is MealSelectedProduct -> EntityProductType.Meal
                is MeasuredSelectedProduct -> EntityProductType.Measured
            }
        )
    }
}