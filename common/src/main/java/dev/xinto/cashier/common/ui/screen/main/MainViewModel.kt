package dev.xinto.cashier.common.ui.screen.main

import androidx.lifecycle.ViewModel
import dev.xinto.cashier.common.ui.navigation.Destination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    private val _destination = MutableStateFlow<Destination>(Destination.Registry)
    val screen = _destination.asStateFlow()

    fun navigate(destination: Destination) {
        _destination.value = destination
    }

}