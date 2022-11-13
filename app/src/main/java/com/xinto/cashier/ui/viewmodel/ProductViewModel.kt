package com.xinto.cashier.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xinto.cashier.domain.model.*
import com.xinto.cashier.domain.repository.RegistryRepositoryImpl
import com.xinto.cashier.ui.screen.EditScreenState
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    val products = mutableStateListOf<SelectableProduct>()

    val selectedProducts = mutableStateMapOf<String, SelectedProduct>()

    var editScreenState by mutableStateOf<EditScreenState>(EditScreenState.Unselected)
        private set

    var total by mutableStateOf(Price.Zero.toString())
        private set

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

    fun payWithCash() {
        viewModelScope.launch {
            RegistryRepositoryImpl.payWithCash(selectedProducts.values.toList())
            clearAll()
        }
    }

    fun payWithCard() {
        viewModelScope.launch {
            RegistryRepositoryImpl.payWithCard(selectedProducts.values.toList())
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
        viewModelScope.launch {
            products.addAll(RegistryRepositoryImpl.getProducts())
        }
    }

}