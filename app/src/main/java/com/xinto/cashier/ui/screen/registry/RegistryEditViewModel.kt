package com.xinto.cashier.ui.screen.registry

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xinto.cashier.domain.model.ParcelableSelectedProduct
import com.xinto.cashier.domain.model.Price
import com.xinto.cashier.domain.model.SelectedProduct
import com.xinto.cashier.domain.repository.RegistryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class RegistryEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: RegistryRepository
) : ViewModel() {

    private val initialProduct = savedStateHandle.get<ParcelableSelectedProduct>(KEY_PRODUCT)!!.product

    private val _count = MutableStateFlow(initialProduct.countAsString)
    val count = _count.asStateFlow()

    val product = count.map {
        initialProduct.parseNewCount(it)
    }.stateIn(
        scope = viewModelScope,
        initialValue = initialProduct,
        started = SharingStarted.WhileSubscribed(5000)
    )

    fun popDigit() {
        _count.update {
            it.dropLast(1)
        }
    }

    fun appendDigit(digit: String) {
        _count.update {
            it + digit
        }
    }

    fun increment() {
        _count.update {
            val countInt = it.toIntOrNull()
                ?: return@update it
            (countInt + 1).toString()
        }
    }

    fun decrement() {
        _count.update {
            val countInt = it.toIntOrNull()
                ?: return@update it
            (countInt - 1).toString()
        }
    }

    fun save() {
        product.value?.let {
            repository.replaceProduct(it)
        }
    }

    companion object {
        const val KEY_PRODUCT = "product"
    }

}