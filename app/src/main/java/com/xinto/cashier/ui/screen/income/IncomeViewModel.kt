package com.xinto.cashier.ui.screen.income

import androidx.lifecycle.ViewModel
import com.xinto.cashier.domain.model.StatusMode
import com.xinto.cashier.domain.model.price
import com.xinto.cashier.domain.repository.DailyStatusRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class IncomeViewModel(
    private val repository: DailyStatusRepository
) : ViewModel() {

    private val _statusMode = MutableStateFlow(StatusMode.CardMeal)
    val statusMode = _statusMode.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val products = statusMode.flatMapLatest {
        repository.observeProducts(it)
    }

    val pricePerMode = products.map { it.price() }

    val price = repository.observeProducts().map { it.price() }

    fun updateStatusMode(newStatusMode: StatusMode) {
        _statusMode.value = newStatusMode
    }

}