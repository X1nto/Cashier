package dev.xinto.cashier.common.ui.screen.registry

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dev.xinto.cashier.common.domain.model.Price
import dev.xinto.cashier.common.domain.repository.RegistryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking

class RegistryCashViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: RegistryRepository
) : ViewModel() {

    private val price = savedStateHandle.get<Price>(KEY_PRICE)!!

    private val _cash = MutableStateFlow(price.toString())
    val cash = _cash.asStateFlow()

    val change = cash.map {
        val nonNullCash = Price.fromString(it)
            ?: return@map null

        if (nonNullCash < price) return@map null

        (nonNullCash - price).toString()
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