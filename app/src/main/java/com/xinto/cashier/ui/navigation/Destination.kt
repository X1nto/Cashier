package com.xinto.cashier.ui.navigation

sealed interface Destination {
    data object Registry : Destination
    data object Status : Destination
}