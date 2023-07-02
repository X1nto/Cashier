package com.xinto.cashier.ui.screen

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xinto.cashier.R
import com.xinto.cashier.domain.model.BottleSelectedProduct
import com.xinto.cashier.domain.model.MealSelectedProduct
import com.xinto.cashier.domain.model.MeasuredSelectedProduct
import com.xinto.cashier.domain.model.Price
import com.xinto.cashier.domain.model.SelectableProduct
import com.xinto.cashier.domain.model.SelectedProduct
import com.xinto.cashier.ui.component.DangerButton
import com.xinto.cashier.ui.component.DangerIconButton
import com.xinto.cashier.ui.component.Dialog
import com.xinto.cashier.ui.component.Icon
import com.xinto.cashier.ui.component.IconButton
import com.xinto.cashier.ui.component.LargeDangerIconButton
import com.xinto.cashier.ui.component.LargeSuccessIconButton
import com.xinto.cashier.ui.component.ListItem
import com.xinto.cashier.ui.component.LocalContentColor
import com.xinto.cashier.ui.component.SuccessButton
import com.xinto.cashier.ui.component.Text
import com.xinto.cashier.ui.component.ThreePaneLayout
import com.xinto.cashier.ui.component.dividedItems
import com.xinto.cashier.ui.viewmodel.ProductViewModel

sealed interface EditScreenState {
    @Stable
    object Unselected : EditScreenState

    @Stable
    data class Selected(val selectedProduct: SelectedProduct) : EditScreenState
}

@Immutable
sealed interface ProductsState {
    @Immutable
    object Loading : ProductsState

    @Stable
    @JvmInline
    value class Success(val products: List<SelectableProduct>) : ProductsState

    @Immutable
    object Error : ProductsState
}

@Composable
fun RegistryScreen(
    viewModel: ProductViewModel,
) {
    ThreePaneLayout(
        paneOne = {
            Column {
                Column {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("პროდუქტები", style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Medium))
                        Icon(
                            modifier = Modifier
                                .size(44.dp)
                                .clickable(onClick = viewModel::refresh),
                            painter = painterResource(id = R.drawable.ic_refresh)
                        )
                    }
                    Box(
                        Modifier
                            .height(1.dp)
                            .background(Color.LightGray)
                            .fillMaxWidth())
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    propagateMinConstraints = false,
                    contentAlignment = Alignment.Center
                ) {
                    when (val state = viewModel.state) {
                        is ProductsState.Loading -> {
                            Text("მიმდინარეობს ჩატვირთვა...", style = TextStyle(fontSize = 28.sp))
                        }
                        is ProductsState.Error -> {
                            Text("შეცდომა ჩატვირთვისას!", style = TextStyle(fontSize = 28.sp))
                        }
                        is ProductsState.Success -> {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                dividedItems(state.products) { product ->
                                    ListItem(
                                        title = { Text(product.name) },
                                        subtitle = {
                                            Text("ფასი: ${product.price}")
                                        },
                                        trailing = {
                                            IconButton(onClick = { viewModel.selectProduct(product) }) {
                                                Icon(painterResource(R.drawable.ic_add))
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

            }
        },
        paneTwo = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                dividedItems(viewModel.selectedProducts.values.toList()) { product ->
                    ListItem(
                        title = {
                            Text(product.name)
                        },
                        subtitle = {
                            when (product) {
                                is MealSelectedProduct -> {
                                    Text("რაოდენობა: ${product.meals}")
                                }
                                is MeasuredSelectedProduct -> {
                                    Text("წონა: ${product.kilos}კგ")
                                }
                                is BottleSelectedProduct -> {
                                    Text("რაოდენობა: ${product.bottles}")
                                }
                            }
                        },
                        trailing = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = { viewModel.enterEditScreen(product.name) }) {
                                    Icon(painterResource(R.drawable.ic_edit))
                                }
                                DangerIconButton(onClick = { viewModel.removeProduct(product.name) }) {
                                    Icon(painterResource(R.drawable.ic_delete))
                                }
                            }
                        }
                    )
                }
            }
        },
        paneThree = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp, bottom = 8.dp, top = 12.dp, start = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
            ) {
                LargeDangerIconButton(onClick = viewModel::clearAll) {
                    Icon(painterResource(R.drawable.ic_close))
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                ) {
                    Text("ჯამი: ${viewModel.total}", style = TextStyle(fontSize = 24.sp))
                }
                LargeSuccessIconButton(onClick = viewModel::payWithCard) {
                    Icon(painterResource(R.drawable.ic_credit_card))
                }
                LargeSuccessIconButton(onClick = viewModel::enterChangeScreen) {
                    Icon(painterResource(R.drawable.ic_payments))
                }
            }
        }
    )
    EditDialog(viewModel)
    ChangeDialog(viewModel)
}

sealed interface ChangeState {
    object Unselected : ChangeState
    data class Change(val price: Price) : ChangeState
}

@Composable
private fun ChangeDialog(
    viewModel: ProductViewModel
) {
    val state = viewModel.changeState
    if (state is ChangeState.Change) {
        Dialog(
            confirmButton = {
                SuccessButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        viewModel.payWithCash()
                        viewModel.exitChangeScreen()
                    },
                ) {
                    Text("შენახვა")
                }
            },
            dismissButton = {
                DangerButton(
                    modifier = Modifier.weight(1f),
                    onClick = viewModel::exitChangeScreen
                ) {
                    Text("გაუქმება")
                }
            },
            onDismissRequest = viewModel::exitChangeScreen,
            title = { Text("ხურდა") },
            subtitle = {

            },
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                var textValue by remember { mutableStateOf("") }
                var changeValue by remember { mutableStateOf("0ლ") }
                var shouldShowChange by remember { mutableStateOf(false) }
                LaunchedEffect(textValue) {
                    val parsedValue = textValue.toDoubleOrNull()
                    if (parsedValue != null && parsedValue >= state.price.price) {
                        shouldShowChange = true
                        changeValue = "${parsedValue - state.price.price}ლ"
                    } else {
                        shouldShowChange = false
                    }
                }
                Row(
                    modifier = Modifier.height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    BasicTextField(
                        modifier = Modifier.fillMaxHeight(),
                        value = textValue,
                        onValueChange = {
                            textValue = it
                        },
                        maxLines = 1,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = TextStyle(fontSize = 24.sp, textAlign = TextAlign.Center),
                        decorationBox = {
                            Box(
                                modifier = Modifier
                                    .heightIn(48.dp)
                                    .border(1.dp, Color.LightGray),
                                contentAlignment = Alignment.Center
                            ) {
                                it()
                            }
                        }
                    )
                }
                Text(
                    modifier = Modifier
                        .align(Alignment.End),
                    text = changeValue,
                    style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.SemiBold)
                )
            }
        }
    }
}

@Composable
private fun EditDialog(
    viewModel: ProductViewModel,
) {
    val state = viewModel.editScreenState
    if (state is EditScreenState.Selected) {
        var product by remember { mutableStateOf(state.selectedProduct) }
        var confirmEnabled by remember { mutableStateOf(true) }
        var increaseEnabled by remember { mutableStateOf(true) }
        var decreaseEnabled by remember { mutableStateOf(true) }
        var showPrice by remember { mutableStateOf(true) }
        Dialog(
            onDismissRequest = viewModel::exitEditScreen,
            title = { Text(state.selectedProduct.name) },
            subtitle = {
                when (product) {
                    is MealSelectedProduct, is BottleSelectedProduct -> {
                        Text("ჩაწერეთ რაოდენობა")
                    }
                    is MeasuredSelectedProduct -> {
                        Text("ჩაწერეთ წონა")
                    }
                }
            },
            confirmButton = {
                SuccessButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        viewModel.replaceProduct(product)
                        viewModel.exitEditScreen()
                    },
                    enabled = confirmEnabled
                ) {
                    Text("შენახვა")
                }
            },
            dismissButton = {
                DangerButton(
                    modifier = Modifier.weight(1f),
                    onClick = viewModel::exitEditScreen
                ) {
                    Text("გაუქმება")
                }
            },
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                var textValue by remember { mutableStateOf(product.countAsString) }
                Row(
                    modifier = Modifier.height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    BasicTextField(
                        modifier = Modifier.fillMaxHeight(),
                        value = textValue,
                        onValueChange = {
                            textValue = it
                            val nextProduct = product.parseNewCount(textValue)
                            if (nextProduct != null) {
                                product = nextProduct
                                confirmEnabled = true
                                increaseEnabled = true
                                decreaseEnabled = true
                                showPrice = true
                            } else {
                                confirmEnabled = false
                                increaseEnabled = false
                                decreaseEnabled = false
                                showPrice = false
                            }
                        },
                        maxLines = 1,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = TextStyle(fontSize = 24.sp, textAlign = TextAlign.Center),
                        decorationBox = {
                            Box(
                                modifier = Modifier
                                    .heightIn(48.dp)
                                    .border(1.dp, Color.LightGray),
                                contentAlignment = Alignment.Center
                            ) {
                                it()
                            }
                        }
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        PriceModifierButton(
                            onClick = {
                                product = product.increased()
                                textValue = product.countAsString
                            },
                            enabled = increaseEnabled
                        ) {
                            Icon(painterResource(R.drawable.ic_add))
                        }
                        PriceModifierButton(
                            onClick = {
                                val decreased = product.decreased()
                                if (decreased != null) {
                                    product = decreased
                                }
                                textValue = product.countAsString
                            },
                            enabled = decreaseEnabled && product.canDecrease
                        ) {
                            Icon(painterResource(R.drawable.ic_remove))
                        }
                    }
                }
                Text(
                    modifier = Modifier
                        .align(Alignment.End)
                        .graphicsLayer {
                            alpha = if (showPrice) 1f else 0f
                        },
                    text = product.price.toString(),
                    style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.SemiBold)
                )
            }
        }
    }
}

@Composable
private fun PriceModifierButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    Box(
        modifier = Modifier
            .graphicsLayer {
                alpha = if (enabled) {
                    if (isPressed) 0.7f else 1f
                } else 0.5f
            }
            .height(36.dp)
            .width(72.dp)
            .background(Color(0xFFFFA500))
            .clickable(
                onClick = onClick,
                enabled = enabled,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = null
            ),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(LocalContentColor provides Color.Black) {
            content()
        }
    }
}