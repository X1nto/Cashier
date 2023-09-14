package com.xinto.cashier.ui.screen.income

import androidx.lifecycle.ViewModel
import com.xinto.cashier.domain.repository.DailyStatusRepository
import kotlinx.coroutines.runBlocking

class IncomeClearViewModel(
    private val repository: DailyStatusRepository
) : ViewModel() {

    fun clearDatabase() {
        runBlocking {
            repository.clear()
        }
    }

}