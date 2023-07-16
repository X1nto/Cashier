package com.xinto.cashier.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xinto.cashier.R

@Composable
fun PaneLayout(
    header: @Composable () -> Unit,
    main: @Composable () -> Unit,
    details: @Composable () -> Unit,
    footer: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        Column(modifier = Modifier.weight(1f)) {
            header()
            HorizontalDivider()
            Box(modifier = Modifier.weight(1f)) {
                main()
            }
        }
        VerticalDivider(width = 2.dp)
        Column(
            modifier = Modifier
                .weight(1.2f)
                .background(Color.White)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                details()
            }
            HorizontalDivider()
            Box(modifier = Modifier.background(Color.White)) {
                footer()
            }
        }
    }
}

@Composable
fun PaneHeader(
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null,
    title: @Composable () -> Unit,
) {
    Row(
        modifier = modifier
            .padding(12.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides TextStyle(fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
        ) {
            title()
        }
        if (action != null) {
            action()
        }
    }
}