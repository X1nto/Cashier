package com.xinto.cashier.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalDivider(
    modifier: Modifier = Modifier,
    height: Dp = 1.dp
) {
    Box(
        modifier
            .fillMaxWidth()
            .height(height)
            .background(Color.LightGray))
}

@Composable
fun VerticalDivider(
    modifier: Modifier = Modifier,
    width: Dp = 1.dp
) {
    Box(
        modifier
            .fillMaxHeight()
            .width(width)
            .background(Color.LightGray))
}