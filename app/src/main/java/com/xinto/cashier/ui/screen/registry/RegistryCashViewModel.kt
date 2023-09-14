package com.xinto.cashier.ui.screen.registry

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xinto.cashier.domain.model.ParcelableSelectedProduct
import com.xinto.cashier.domain.model.Price
import com.xinto.cashier.domain.model.asPrice
import com.xinto.cashier.domain.repository.RegistryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class RegistryCashViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: RegistryRepository
) : ViewModel() {

    private val price = savedStateHandle.get<Double>(KEY_PRICE)!!

    private val _cash = MutableStateFlow(String.format("%.2f", price))
    val cash = _cash.asStateFlow()

    val change = cash.map {
        val nonNullCash = it.toDoubleOrNull()
            ?: return@map null

        if (nonNullCash < price) return@map null

        nonNullCash - price
    }

    fun popDigit() {
        _cash.update {
            it.dropLast(1)
        }
    }

    fun appendDigit(digit: String) {
        _cash.update {
            it + digit
        }
    }

    fun pay() {
        runBlocking {
            repository.payWithCash()
        }
    }

    companion object {
        val KEY_PRICE = "price"
    }

}