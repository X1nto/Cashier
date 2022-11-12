package com.xinto.cashier.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

enum class Screen {
    Registry, Daily
}

class MainViewModel : ViewModel() {

    var currentScreen by mutableStateOf(Screen.Registry)
        private set

    fun switchToRegistry() {
        currentScreen = Screen.Registry
    }

    fun switchToDaily() {
        currentScreen = Screen.Daily
    }

}