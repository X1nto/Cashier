package com.xinto.cashier.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.dp

@Composable
fun IconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color.Blue,
    contentColor: Color = Color.White,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) = IconButtonImpl(
    modifier = modifier.size(width = 72.dp, height = 72.dp),
    onClick = onClick,
    color = color,
    contentColor = contentColor,
    enabled = enabled,
    content = content
)

@Composable
fun DangerIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color.Red,
    contentColor: Color = Color.White,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) = IconButton(
    modifier = modifier,
    onClick = onClick,
    color = color,
    contentColor = contentColor,
    enabled = enabled,
    content = content
)

@Composable
fun SuccessIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color.Green,
    contentColor: Color = Color.White,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) = IconButton(
    modifier = modifier,
    onClick = onClick,
    color = color,
    contentColor = contentColor,
    enabled = enabled,
    content = content
)

@Composable
fun WarningIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color.Yellow,
    contentColor: Color = Color.Black,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) = IconButton(
    modifier = modifier,
    onClick = onClick,
    color = color,
    contentColor = contentColor,
    enabled = enabled,
    content = content
)

@Composable
fun LargeIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color.Blue,
    contentColor: Color = Color.White,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) = IconButtonImpl(
    modifier = modifier.size(width = 96.dp, height = 96.dp),
    onClick = onClick,
    color = color,
    contentColor = contentColor,
    enabled = enabled,
    content = content
)


@Composable
fun LargeDangerIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color.Red,
    contentColor: Color = Color.White,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) = LargeIconButton(
    modifier = modifier,
    onClick = onClick,
    color = color,
    contentColor = contentColor,
    enabled = enabled,
    content = content
)

@Composable
fun LargeSuccessIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color.Green,
    contentColor: Color = Color.White,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) = LargeIconButton(
    modifier = modifier,
    onClick = onClick,
    color = color,
    contentColor = contentColor,
    enabled = enabled,
    content = content
)

@Composable
fun LargeWarningIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color.Yellow,
    contentColor: Color = Color.Black,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) = LargeIconButton(
    modifier = modifier,
    onClick = onClick,
    color = color,
    contentColor = contentColor,
    enabled = enabled,
    content = content
)

@Composable
private fun IconButtonImpl(
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
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            content()
        }
    }
}