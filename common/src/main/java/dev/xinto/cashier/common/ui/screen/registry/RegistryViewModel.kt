package dev.xinto.cashier.common.ui.screen.registry

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.xinto.cashier.common.domain.DomainCacheableState
import dev.xinto.cashier.common.domain.model.Price
import dev.xinto.cashier.common.domain.model.Product
import dev.xinto.cashier.common.domain.model.SelectedProduct
import dev.xinto.cashier.common.domain.repository.ProductsRepository
import dev.xinto.cashier.common.domain.repository.RegistryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking

class RegistryViewModel(
    private val registryRepository: RegistryRepository,
    private val productsRepository: ProductsRepository
) : ViewModel() {

    private val products = productsRepository.getProducts()
    val state = products.flow
        .map {
            when (it) {
                is DomainCacheableState.Loaded -> RegistryState.Success(it.value)
                is DomainCacheableState.Loading -> RegistryState.Loading
            }
        }.catch {
            emit(RegistryState.Error)
            Log.d("RegistryViewModel", "state", it)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RegistryState.Loading,
        )

    val selectedProducts = registryRepository.observeSelectedProducts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf(),
        )

    val price = registryRepository.observePrice()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Price.Zero
        )

    fun refresh() {
        products.refresh()
    }

    fun selectProduct(product: Product) {
        registryRepository.selectProduct(product)
    }

    fun removeProduct(selectedProduct: SelectedProduct) {
        registryRepository.removeProduct(selectedProduct.name)
    }

    fun clearProducts() {
        registryRepository.clearProducts()
    }

    fun payWithCard() {
        runBlocking {
            registryRepository.payWithCard()
        }
    }
}