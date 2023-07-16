package com.xinto.cashier.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xinto.cashier.domain.model.Price
import com.xinto.cashier.domain.model.Result
import com.xinto.cashier.domain.model.SelectableProduct
import com.xinto.cashier.domain.model.SelectedProduct
import com.xinto.cashier.domain.model.price
import com.xinto.cashier.domain.model.toSelectedProduct
import com.xinto.cashier.domain.repository.RegistryRepository
import com.xinto.cashier.ui.screen.ChangeState
import com.xinto.cashier.ui.screen.EditScreenState
import com.xinto.cashier.ui.screen.ProductsState
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: RegistryRepository
) : ViewModel() {

    val selectedProducts = mutableStateMapOf<String, SelectedProduct>()

    var state by mutableStateOf<ProductsState>(ProductsState.Loading)
        private set

    var editScreenState by mutableStateOf<EditScreenState>(EditScreenState.Unselected)
        private set

    var changeState by mutableStateOf<ChangeState>(ChangeState.Unselected)
        private set

    var total by mutableStateOf(Price.Zero.toString())
        private set

    fun refresh(forceRefresh: Boolean = true) {
        viewModelScope.launch {
            state = ProductsState.Loading

            state = when (val products = repository.getProducts(forceRefresh)) {
                is Result.Success -> ProductsState.Success(products.data)
                is Result.Error -> ProductsState.Error
            }
        }
    }

    fun selectProduct(product: SelectableProduct) {
        val existingSameProduct = selectedProducts[product.name]
        if (existingSameProduct == null) {
            selectedProducts[product.name] = product.toSelectedProduct()
        } else {
            selectedProducts[product.name] = existingSameProduct.increased()
        }
        calculateTotal()
    }

    fun replaceProduct(selectedProduct: SelectedProduct) {
        if (selectedProducts[selectedProduct.name] != null) {
            selectedProducts[selectedProduct.name] = selectedProduct
        }
        calculateTotal()
    }

    fun removeProduct(id: String) {
        selectedProducts.remove(id)
        calculateTotal()
    }

    fun enterEditScreen(id: String) {
        val selectedProduct = selectedProducts[id]
        if (selectedProduct != null) {
            editScreenState = EditScreenState.Selected(selectedProduct)
        }
    }

    fun exitEditScreen() {
        editScreenState = EditScreenState.Unselected
    }

    fun enterChangeScreen() {
        changeState = ChangeState.Change(price = selectedProducts.values.price())
    }

    fun exitChangeScreen() {
        changeState = ChangeState.Unselected
    }

    fun payWithCash() {
        viewModelScope.launch {
            repository.payWithCash(selectedProducts.values.toList())
            clearAll()
        }
    }

    fun payWithCard() {
        viewModelScope.launch {
            repository.payWithCard(selectedProducts.values.toList())
            clearAll()
        }
    }

    fun clearAll() {
        selectedProducts.clear()
        calculateTotal()
    }

    private fun calculateTotal() {
        total = selectedProducts.values.price().toString()
    }

    init {
        refresh(forceRefresh = false)
    }

}