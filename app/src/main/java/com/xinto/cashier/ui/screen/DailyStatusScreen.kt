package com.xinto.cashier.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xinto.cashier.domain.model.BottleStatusProduct
import com.xinto.cashier.domain.model.MealStatusProduct
import com.xinto.cashier.ui.component.*
import com.xinto.cashier.ui.viewmodel.DailyStatusViewModel

enum class DailyStatusState(val title: String) {
    CashMeals("საჭმელი ქეშით"),
    CardMeals("საჭმელი ბარათით"),
    CashDrinks("სასმელი ქეშით"),
    CardDrinks("სასმელი ბარათით")
}

@Composable
fun DailyStatusScreen(viewModel: DailyStatusViewModel) {
    ThreePaneLayout(
        paneOne = {
            Column(modifier = Modifier.fillMaxSize()) {
                DailyStatusState.values().forEachIndexed { index, state ->
                    Column(modifier = Modifier.weight(1f)) {
                        if (index != 0) {
                            Spacer(
                                Modifier
                                    .fillMaxWidth()
                                    .background(Color.LightGray)
                                    .height(1.dp))
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
        paneTwo = {
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
        paneThree = {
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