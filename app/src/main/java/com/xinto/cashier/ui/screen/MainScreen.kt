package com.xinto.cashier.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xinto.cashier.R
import com.xinto.cashier.ui.component.Icon
import com.xinto.cashier.ui.component.Tab
import com.xinto.cashier.ui.viewmodel.MainViewModel
import com.xinto.cashier.ui.viewmodel.Screen

@Composable
fun MainScreen() {
    val viewModel: MainViewModel = viewModel()
    Row {
        Column(modifier = Modifier.width(72.dp)) {
            Tab(
                selected = viewModel.currentScreen == Screen.Registry,
                onSelect = viewModel::switchToRegistry
            ) {
                Icon(painter = painterResource(R.drawable.ic_point_of_sale))
            }
            Tab(
                selected = viewModel.currentScreen == Screen.Daily,
                onSelect = viewModel::switchToDaily
            ) {
                Icon(painter = painterResource(R.drawable.ic_history))
            }
        }
        Box(
            Modifier
                .fillMaxHeight()
                .width(2.dp)
                .background(Color.LightGray))
        Box(modifier = Modifier.weight(1f)) {
            when (viewModel.currentScreen) {
                Screen.Registry -> {
                    RegistryScreen(viewModel())
                }
                Screen.Daily -> {
                    DailyStatusScreen(viewModel())
                }
            }
        }
    }
}