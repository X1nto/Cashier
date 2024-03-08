package dev.xinto.cashier.common.domain.repository

import dev.xinto.cashier.common.domain.Cacheable
import dev.xinto.cashier.common.domain.model.Price
import dev.xinto.cashier.common.domain.model.Product
import dev.xinto.cashier.common.domain.model.ProductDiff
import dev.xinto.cashier.common.domain.model.ProductType
import dev.xinto.cashier.common.network.registry.RegistryApi
import dev.xinto.cashier.common.network.registry.model.ApiProduct
import dev.xinto.cashier.common.network.registry.model.ApiProductPatches
import dev.xinto.cashier.common.network.registry.model.ApiProductType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class ProductsRepository(private val registryApi: RegistryApi) {

    private val products = Cacheable {
        registryApi.getProducts(true).map {
            Product(
                id = it.id,
                name = it.name,
                price = Price(it.price),
                type = when (it.type) {
                    ApiProductType.Drink -> ProductType.Bottle
                    ApiProductType.Food -> ProductType.Meal
                }
            )
        }
    }

    private val diffs = MutableStateFlow<Map<String, ProductDiff>>(mapOf())

    fun getProducts() = products

    fun getDiffs() = diffs

    fun putDiff(diff: ProductDiff) {
        updateDiff {
            this[diff.id] = diff
        }
    }

    fun removeDiff(id: String) {
        updateDiff {
            remove(id)
        }
    }

    suspend fun sendDiffs(): Boolean {
        val removes = mutableListOf<String>()
        val adds = mutableListOf<ApiProduct>()
        val edits = mutableListOf<ApiProduct>()

        diffs.value.values.forEach {
            when (it) {
                is ProductDiff.Add -> {
                    adds.add(
                        ApiProduct(
                            id = it.id,
                            name = it.name,
                            price = it.price.value,
                            type = it.type.toApiType()
                        )
                    )
                }
                is ProductDiff.Edit -> {
                    edits.add(
                        ApiProduct(
                            id = it.id,
                            name = it.name,
                            price = it.price.value,
                            type = it.type.toApiType()
                        )
                    )
                }
                is ProductDiff.Remove -> {
                    removes.add(it.id)
                }
            }
        }

        return registryApi.updateProducts(
            ApiProductPatches(
                add = adds,
                remove = removes,
                edit = edits
            )
        ).also {
            if (it) {
                diffs.value = emptyMap()
            }
        }
    }

    private inline fun updateDiff(
        update: MutableMap<String, ProductDiff>.() -> Unit
    ) {
        diffs.update {
            val map = it.toMutableMap()
            map.update()
            map
        }
    }

    private fun ProductType.toApiType(): ApiProductType {
        return when (this) {
            ProductType.Meal -> ApiProductType.Food
            ProductType.Bottle -> ApiProductType.Drink
        }
    }

}