package com.xinto.cashier.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun Icon(
    painter: Painter,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
) {
    BoxWithConstraints {
        Image(
            modifier = modifier.size(
                width = maxWidth - ((maxWidth / 24) * 8),
                height = maxWidth - ((maxWidth / 24) * 8)
            ),
            painter = painter,
            contentDescription = null,
            colorFilter = ColorFilter.tint(tint)
        )
    }
}