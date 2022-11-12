package com.xinto.cashier.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RadioButton(
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .requiredSize(24.dp)
            .aspectRatio(1f / 1f)
            .clip(CircleShape)
            .border(1.dp, if (selected) Color.Blue else Color.LightGray, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(1f / 1f)
                .padding(4.dp)
                .clip(CircleShape)
                .background(if (selected) Color.Blue else Color.Transparent)
        )
    }
}