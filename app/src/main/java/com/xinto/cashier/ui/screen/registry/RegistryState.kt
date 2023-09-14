package com.xinto.cashier.ui.screen.registry

import com.xinto.cashier.domain.model.SelectableProduct

sealed interface RegistryState {
    data object Loading : RegistryState

    @JvmInline
    value class Success(val products: List<SelectableProduct>) : RegistryState

    data object Error : RegistryState
}