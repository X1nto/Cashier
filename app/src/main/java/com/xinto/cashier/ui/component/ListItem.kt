package com.xinto.cashier.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

inline fun <T> LazyListScope.dividedItems(
    items: List<T>,
    noinline key: ((item: T) -> Any)? = null,
    noinline contentType: (item: T) -> Any? = { null },
    crossinline itemContent: @Composable LazyItemScope.(item: T) -> Unit
) = itemsIndexed(
    items = items,
    key = if (key != null) { _, item -> key(item) } else null,
    contentType = { _: Int, item: T -> contentType(item) }
) { index, item ->
    if (index != 0) {
        Box(
            Modifier
                .fillParentMaxWidth()
                .height(1.dp)
                .background(Color.LightGray)
        )
    }
    itemContent(item)
}

@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    subtitle: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                icon()
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                CompositionLocalProvider(
                    LocalTextStyle provides TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium
                    )
                ) {
                    title()
                }
                if (subtitle != null) {
                    CompositionLocalProvider(
                        LocalTextStyle provides TextStyle(fontSize = 18.sp),
                        LocalContentColor provides LocalContentColor.current.copy(alpha = 0.8f)
                    ) {
                        subtitle()
                    }
                }
            }
            if (trailing != null) {
                trailing()
            }
        }
    }
}