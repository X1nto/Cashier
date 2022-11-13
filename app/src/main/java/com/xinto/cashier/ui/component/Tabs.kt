package com.xinto.cashier.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RowScope.Tab(
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .weight(1f)
            .sizeIn(minHeight = 48.dp)
            .clickable(onClick = onSelect)
            .alpha(if (selected) 1f else 0.5f),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        ) {
            content()
        }
    }
}

@Composable
fun ColumnScope.Tab(
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .weight(1f)
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .alpha(if (selected) 1f else 0.5f),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        ) {
            content()
        }
    }
}