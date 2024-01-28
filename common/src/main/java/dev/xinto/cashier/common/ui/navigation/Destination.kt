package dev.xinto.cashier.common.ui.navigation

sealed interface Destination {
    data object Registry : Destination
    data object Status : Destination
}