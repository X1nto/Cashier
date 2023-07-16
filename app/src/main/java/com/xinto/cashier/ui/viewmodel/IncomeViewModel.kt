package com.xinto.cashier.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xinto.cashier.domain.model.Price
import com.xinto.cashier.domain.model.StatusProduct
import com.xinto.cashier.domain.model.price
import com.xinto.cashier.domain.repository.DailyStatusRepository
import com.xinto.cashier.ui.screen.DailyStatusState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class IncomeViewModel(
    private val repository: DailyStatusRepository
) : ViewModel() {

    var state by mutableStateOf(DailyStatusState.CashMeals)
        private set

    val items = mutableStateListOf<StatusProduct>()
    var total by mutableStateOf(Price.Zero.toString())
        private set

    private var job = repository.observeCashMeals().setup()

    fun select(state: DailyStatusState) {
        this.state = state
        job.cancel()
        job = when (state) {
            DailyStatusState.CashMeals -> repository.observeCashMeals()
            DailyStatusState.CardMeals -> repository.observeCardMeals()
            DailyStatusState.CashDrinks -> repository.observeCashDrinks()
            DailyStatusState.CardDrinks -> repository.observeCardDrinks()
        }.setup()
    }

    fun clear() {
        viewModelScope.launch {
            repository.clear()
        }
    }

    private fun Flow<List<StatusProduct>>.setup(): Job {
        return this.onEach {
            items.clear()
            items.addAll(it)

            total = it.price().toString()
        }.launchIn(viewModelScope)
    }

    override fun onCleared() {
        job.cancel()
    }

}