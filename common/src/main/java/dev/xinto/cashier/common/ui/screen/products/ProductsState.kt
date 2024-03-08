package dev.xinto.cashier.common.ui.screen.products

import dev.xinto.cashier.common.domain.model.ProductItem

sealed interface ProductsState {

    data object Loading : ProductsState

    data class Success(val products: List<ProductItem>) : ProductsState

    data object Error : ProductsState
}