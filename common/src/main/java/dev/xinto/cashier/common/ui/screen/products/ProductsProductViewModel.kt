package dev.xinto.cashier.common.ui.screen.products

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.xinto.cashier.common.domain.model.Price
import dev.xinto.cashier.common.domain.model.ProductDiff
import dev.xinto.cashier.common.domain.model.ProductItem
import dev.xinto.cashier.common.domain.model.ProductType
import dev.xinto.cashier.common.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.util.UUID

class ProductsProductViewModel(
    savedStateHandle: SavedStateHandle,
    private val productsRepository: ProductsRepository
) : ViewModel() {

    private val _product: ProductItem? = savedStateHandle[KEY_PRODUCT]

    val name = MutableStateFlow(_product?.name ?: "")
    val price = MutableStateFlow(_product?.price?.toString() ?: "")
    private val parsedPrice = price
        .map { Price.fromString(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null,
        )

    val canSave = combine(name, parsedPrice) { name, price ->
        name.isNotBlank() && price != null
    }

    fun updateName(newName: String) {
        name.value = newName
    }

    fun appendDigit(digit: String) {
        price.update {
            it + digit
        }
    }

    fun popDigit() {
        price.update {
            it.dropLast(1)
        }
    }

    fun save() {
        productsRepository.putDiff(
            if (_product != null) {
                ProductDiff.Edit(
                    id = _product.id,
                    name = name.value,
                    price = parsedPrice.value!!,
                    type = ProductType.Meal
                )
            } else {
                ProductDiff.Add(
                    id = UUID.randomUUID().toString(),
                    name = name.value,
                    price = parsedPrice.value!!,
                    type = ProductType.Meal
                )
            }
        )
    }

    companion object {
        const val KEY_PRODUCT = "product"
    }
}