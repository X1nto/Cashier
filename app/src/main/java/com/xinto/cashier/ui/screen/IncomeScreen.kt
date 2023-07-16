package com.xinto.cashier.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xinto.cashier.R
import com.xinto.cashier.domain.model.BottleStatusProduct
import com.xinto.cashier.domain.model.MealStatusProduct
import com.xinto.cashier.ui.component.Button
import com.xinto.cashier.ui.component.DangerButton
import com.xinto.cashier.ui.component.Dialog
import com.xinto.cashier.ui.component.Icon
import com.xinto.cashier.ui.component.ListItem
import com.xinto.cashier.ui.component.PaneHeader
import com.xinto.cashier.ui.component.PaneLayout
import com.xinto.cashier.ui.component.RadioButton
import com.xinto.cashier.ui.component.SmallIconButton
import com.xinto.cashier.ui.component.Text
import com.xinto.cashier.ui.component.dividedItems
import com.xinto.cashier.ui.viewmodel.IncomeViewModel

enum class DailyStatusState(val title: String) {
    CashMeals("საჭმელი ქეშით"),
    CardMeals("საჭმელი ბარათით"),
    CashDrinks("სასმელი ქეშით"),
    CardDrinks("სასმელი ბარათით")
}

@Composable
fun IncomeScreen(viewModel: IncomeViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        ClearDialog(
            onDismissRequest = { showDialog = false },
            onConfirm = {
                viewModel.clear()
                showDialog = false
            }
        )
    }
    PaneLayout(
        header = {
            PaneHeader(
                action = {
                    SmallIconButton(onClick = {
                        showDialog = true
                    }) {
                        Icon(painterResource(id = R.drawable.ic_delete))
                    }
                }
            ) {
                Text("შემოსავალი")
            }
        },
        main = {
            Column(modifier = Modifier.fillMaxSize()) {
                DailyStatusState.values().forEachIndexed { index, state ->
                    Column(modifier = Modifier.weight(1f)) {
                        if (index != 0) {
                            Spacer(
                                Modifier
                                    .fillMaxWidth()
                                    .background(Color.LightGray)
                                    .height(1.dp)
                            )
                        }
                        val interactionSource = remember { MutableInteractionSource() }
                        val pressed by interactionSource.collectIsPressedAsState()
                        Box(
                            modifier = Modifier
                                .graphicsLayer {
                                    alpha = if (pressed) 0.7f else 1f
                                }
                                .clickable(
                                    onClick = { viewModel.select(state) },
                                    interactionSource = interactionSource,
                                    indication = null,
                                )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = state.title,
                                    style = TextStyle(fontSize = 28.sp),
                                    modifier = Modifier.weight(1f)
                                )
                                RadioButton(selected = viewModel.state == state)
                            }
                        }
                    }
                }
            }
        },
        details = {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                dividedItems(items = viewModel.items) { item ->
                    ListItem(
                        title = {
                            Text(item.name)
                        },
                        subtitle = {
                            val quantity = when (item) {
                                is MealStatusProduct -> item.meals
                                is BottleStatusProduct -> item.bottles
                            }
                            Text("რაოდენობა: $quantity")
                        },
                        trailing = {
                            Text(item.price.toString(), style = TextStyle(fontSize = 20.sp))
                        }
                    )
                }
            }
        },
        footer = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text("ჯამი: ${viewModel.total}", style = TextStyle(fontSize = 24.sp))
            }
        }
    )
}

@Composable
private fun ClearDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text("ნამდვილად გსურთ მონაცემთა ბაზის გასუფთავება?")
        },
        subtitle = {
            Text("გასუფთავების შემდეგ ვეღარ შეძლებთ მონაცემების დაბრუნებას")
        },
        confirmButton = {
            DangerButton(
                modifier = Modifier.weight(1f),
                onClick = onConfirm
            ) {
                Text("წაშლა")
            }
        },
        dismissButton = {
            Button(
                modifier = Modifier.weight(1f),
                onClick = onDismissRequest
            ) {
                Text("გაუქმება")
            }
        }
    ) {

    }
}