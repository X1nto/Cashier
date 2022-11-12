package com.xinto.cashier.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*

@Composable
fun Dialog(
    onDismissRequest: () -> Unit,
    title: @Composable () -> Unit,
    subtitle: @Composable () -> Unit,
    confirmButton: @Composable RowScope.() -> Unit,
    dismissButton: @Composable RowScope.() -> Unit,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = Modifier.background(Color.White),
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CompositionLocalProvider(
                    LocalTextStyle provides TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium
                    )
                ) {
                    title()
                }
                CompositionLocalProvider(
                    LocalTextStyle provides TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    LocalContentColor provides LocalContentColor.current.copy(alpha = 0.8f)
                ) {
                    Box(modifier = Modifier.padding(top = 8.dp)) {
                        subtitle()
                    }
                }
                Box(modifier = Modifier.padding(top = 16.dp)) {
                    content()
                }
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.LightGray))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically
            ) {
                dismissButton()
                Box(
                    Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .background(Color.LightGray))
                confirmButton()
            }
        }
    }
}