package com.xinto.cashier.domain.model

import androidx.compose.runtime.Stable

fun Iterable<StatusProduct>.price() = priceOf { it.price }

@Stable
sealed interface StatusProduct {
    val name: String
    val price: Price
}

data class MealStatusProduct(
    override val name: String,
    val mealPrice: Price,
    val meals: Int,
) : StatusProduct {
    override val price = mealPrice * meals
}

data class BottleStatusProduct(
    override val name: String,
    val bottlePrice: Price,
    val bottles: Int,
) : StatusProduct {
    override val price = bottlePrice * bottles
}