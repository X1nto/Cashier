package dev.xinto.cashier.common.ui.screen.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.xinto.cashier.common.domain.DomainCacheableState
import dev.xinto.cashier.common.domain.model.Product
import dev.xinto.cashier.common.domain.model.ProductDiff
import dev.xinto.cashier.common.domain.model.ProductItem
import dev.xinto.cashier.common.domain.model.ProductItemStatus
import dev.xinto.cashier.common.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductsViewModel(
    private val productsRepository: ProductsRepository
) : ViewModel() {

    private val products = productsRepository.getProducts()
    private val diffs = productsRepository.getDiffs()

    private val _sendingRequest = MutableStateFlow(false)
    val sendingRequest = _sendingRequest.asStateFlow()

    val state = combine(products.flow, diffs) { products, diffs ->
        if (products is DomainCacheableState.Loading)
            return@combine ProductsState.Loading

        val productsCasted = (products as DomainCacheableState.Loaded<List<Product>>).value
        val productsById = mutableMapOf<String, ProductItem>()
        for (product in productsCasted) {
            productsById[product.id] = ProductItem(
                id = product.id,
                name = product.name,
                price = product.price,
                type = product.type,
                status = ProductItemStatus.Existing
            )
        }
        diffs.values.forEach {
            when (it) {
                is ProductDiff.Remove -> {
                    productsById[it.id]?.let { existing ->
                        productsById[it.id] = existing.copy(status = ProductItemStatus.Removed)
                    }
                }
                is ProductDiff.Edit -> {
                    productsById[it.id] = ProductItem(
                        id = it.id,
                        name = it.name,
                        price = it.price,
                        type = it.type,
                        status = ProductItemStatus.Edited
                    )
                }
                is ProductDiff.Add -> {
                    productsById[it.id] = ProductItem(
                        id = it.id,
                        name = it.name,
                        price = it.price,
                        type = it.type,
                        status = ProductItemStatus.New
                    )
                }
            }
        }
        ProductsState.Success(productsById.values.toList())
    }.catch {
        emit(ProductsState.Error)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProductsState.Loading
    )

    val saveEnabled = diffs.map { it.isNotEmpty() }

    fun restoreItem(id: String) {
        productsRepository.removeDiff(id)
    }

    fun removeItem(id: String) {
        productsRepository.putDiff(ProductDiff.Remove(id))
    }

    fun save() {
        viewModelScope.launch {
            _sendingRequest.value = true
            val status = productsRepository.sendDiffs()
            _sendingRequest.value = false

            
        }
    }
}