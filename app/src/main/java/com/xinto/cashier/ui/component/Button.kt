package com.xinto.cashier.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color.Blue,
    contentColor: Color = Color.White,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) = ButtonImpl(
    modifier = modifier.sizeIn(minWidth = 144.dp, minHeight = 72.dp),
    onClick = onClick,
    color = color,
    contentColor = contentColor,
    enabled = enabled,
    content = content
)

@Composable
fun DangerButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color.Red,
    contentColor: Color = Color.White,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) = Button(
    modifier = modifier,
    onClick = onClick,
    color = color,
    contentColor = contentColor,
    enabled = enabled,
    content = content
)

@Composable
fun SuccessButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color.Green,
    contentColor: Color = Color.White,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) = Button(
    modifier = modifier,
    onClick = onClick,
    color = color,
    contentColor = contentColor,
    enabled = enabled,
    content = content
)

@Composable
fun WarningButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color.Yellow,
    contentColor: Color = Color.Black,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) = Button(
    modifier = modifier,
    onClick = onClick,
    color = color,
    contentColor = contentColor,
    enabled = enabled,
    content = content
)

@Composable
private fun ButtonImpl(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color.Blue,
    contentColor: Color = Color.White,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    Box(
        modifier = modifier
            .graphicsLayer {
                alpha = if (enabled) {
                    if (isPressed) 0.7f else 1f
                } else 0.5f
            }
            .background(color)
            .clickable(
                onClick = onClick,
                enabled = enabled,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = null
            ),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(
            LocalContentColor provides contentColor,
            LocalTextStyle provides TextStyle(
                fontSize = 22.sp
            )
        ) {
            content()
        }
    }
}