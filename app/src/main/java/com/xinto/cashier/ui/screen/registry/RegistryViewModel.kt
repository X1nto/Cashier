package com.xinto.cashier.ui.screen.registry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xinto.cashier.domain.model.Price
import com.xinto.cashier.domain.model.Result
import com.xinto.cashier.domain.model.SelectableProduct
import com.xinto.cashier.domain.model.SelectedProduct
import com.xinto.cashier.domain.repository.RegistryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class RegistryViewModel(
    private val repository: RegistryRepository
) : ViewModel() {

    private val _state = MutableStateFlow<RegistryState>(RegistryState.Loading)
    val state = _state.asStateFlow()

    val selectedProducts = repository.observeSelectedProducts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf(),
        )

    val price = repository.observePrice()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Price.Zero
        )

    fun refresh(force: Boolean = true) {
        viewModelScope.launch {
            _state.value = RegistryState.Loading

            _state.value = when (val result = repository.fetchProducts(force)) {
                is Result.Success -> RegistryState.Success(result.data)
                is Result.Error -> RegistryState.Error
            }
        }
    }

    fun selectProduct(selectableProduct: SelectableProduct) {
        repository.selectProduct(selectableProduct)
    }

    fun removeProduct(selectedProduct: SelectedProduct) {
        repository.removeProduct(selectedProduct.name)
    }

    fun clearProducts() {
        repository.clearProducts()
    }

    fun payWithCard() {
        runBlocking {
            repository.payWithCard()
        }
    }

    init {
        refresh(force = false)
    }
}