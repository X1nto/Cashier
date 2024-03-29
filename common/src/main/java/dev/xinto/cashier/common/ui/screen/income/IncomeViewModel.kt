package dev.xinto.cashier.common.ui.screen.income

import androidx.lifecycle.ViewModel
import dev.xinto.cashier.common.domain.model.StatusMode
import dev.xinto.cashier.common.domain.model.price
import dev.xinto.cashier.common.domain.repository.DailyStatusRepository
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