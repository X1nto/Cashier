package dev.xinto.cashier.common.ui.screen.income

import androidx.lifecycle.ViewModel
import dev.xinto.cashier.common.domain.repository.DailyStatusRepository
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