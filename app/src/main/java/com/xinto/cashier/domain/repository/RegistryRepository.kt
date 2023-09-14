package com.xinto.cashier.domain.repository

import android.util.Log
import com.xinto.cashier.db.entity.EntityPaymentType
import com.xinto.cashier.db.entity.EntityProduct
import com.xinto.cashier.db.entity.EntityProductType
import com.xinto.cashier.db.store.ProductsDao
import com.xinto.cashier.domain.model.BottleSelectableProduct
import com.xinto.cashier.domain.model.BottleSelectedProduct
import com.xinto.cashier.domain.model.MealSelectableProduct
import com.xinto.cashier.domain.model.MealSelectedProduct
import com.xinto.cashier.domain.model.MeasuredSelectedProduct
import com.xinto.cashier.domain.model.Price
import com.xinto.cashier.domain.model.Result
import com.xinto.cashier.domain.model.SelectableProduct
import com.xinto.cashier.domain.model.SelectedProduct
import com.xinto.cashier.domain.model.price
import com.xinto.cashier.domain.model.toSelectedProduct
import com.xinto.cashier.network.registry.RegistryApi
import com.xinto.cashier.network.registry.model.ApiProductType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext


class RegistryRepository(
    private val registryApi: RegistryApi,
    private val productsDao: ProductsDao
) {

    private val selectedProducts = MutableStateFlow(emptyMap<String, SelectedProduct>())

    suspend fun fetchProducts(forceRefresh: Boolean): Result<List<SelectableProduct>> {
        return try {
            val data = registryApi.getProducts(forceRefresh).map {
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
            Log.d("Registry repo", e.stackTraceToString())
            Result.Error
        }
    }

    fun observeSelectedProducts(): Flow<List<SelectedProduct>> {
        return selectedProducts.map { it.values.toList() }
    }

    fun observePrice(): Flow<Price> {
        return selectedProducts.map { it.values.price() }
    }

    fun selectProduct(product: SelectableProduct) {
        selectedProducts.update {
            it.toMutableMap().apply {
                val existing = this[product.name]
                if (existing != null) {
                    this[product.name] = existing.increased()
                } else {
                    this[product.name] = product.toSelectedProduct()
                }
            }
        }
    }

    fun replaceProduct(product: SelectedProduct) {
        selectedProducts.update {
            it.toMutableMap().apply {
                if (this[product.name] != null) {
                    this[product.name] = product
                }
            }
        }
    }

    fun removeProduct(name: String) {
        selectedProducts.update {
            it.toMutableMap().apply {
                remove(name)
            }
        }
    }

    fun clearProducts() {
        selectedProducts.update {
            mutableMapOf()
        }
    }

    suspend fun payWithCard() {
        pay(EntityPaymentType.Card)
    }

    suspend fun payWithCash() {
        pay(EntityPaymentType.Cash)
    }

    private suspend fun pay(paymentType: EntityPaymentType) {
        storeProducts(selectedProducts.value.values.toList(), paymentType)
        clearProducts()
    }

    private suspend fun storeProducts(
        selectedProducts: List<SelectedProduct>,
        entityPaymentType: EntityPaymentType
    ) {
        withContext(Dispatchers.IO) {
            productsDao.putDailyProducts(
                selectedProducts.map {
                    it.toEntityProduct(entityPaymentType)
                }
            )
        }
    }

    private fun SelectedProduct.toEntityProduct(entityPaymentType: EntityPaymentType): EntityProduct {
        return EntityProduct(
            id = name + entityPaymentType.toString(),
            name = name,
            price = when (this) {
                is BottleSelectedProduct -> bottlePrice
                is MealSelectedProduct -> mealPrice
                is MeasuredSelectedProduct -> pricePerKilo
            }.value,
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