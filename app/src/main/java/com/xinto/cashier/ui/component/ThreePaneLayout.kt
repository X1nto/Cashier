package com.xinto.cashier.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ThreePaneLayout(
    paneOne: @Composable () -> Unit,
    paneTwo: @Composable () -> Unit,
    paneThree: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        Box(modifier = Modifier.weight(1f)) {
            paneOne()
        }
        Box(
            Modifier
                .fillMaxHeight()
                .width(2.dp)
                .background(Color.LightGray))
        Column(
            modifier = Modifier
                .weight(1.2f)
                .background(Color.White)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                paneTwo()
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color.LightGray))
            Box(modifier = Modifier.background(Color.White)) {
                paneThree()
            }
        }
    }
}