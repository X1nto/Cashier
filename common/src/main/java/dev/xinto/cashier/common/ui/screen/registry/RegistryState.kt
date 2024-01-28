package dev.xinto.cashier.common.ui.screen.registry

import dev.xinto.cashier.common.domain.model.SelectableProduct

sealed interface RegistryState {
    data object Loading : RegistryState

    @JvmInline
    value class Success(val products: List<SelectableProduct>) : RegistryState

    data object Error : RegistryState
}