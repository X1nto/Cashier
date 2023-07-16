package com.xinto.cashier.domain.model

import androidx.compose.runtime.Stable

enum class SelectableProductType {
    Meal,
    Bottle
}

@Stable
data class SelectableProduct(
    val name: String,
    val price: Price,
    val type: SelectableProductType
)

fun SelectableProduct.toSelectedProduct(): SelectedProduct {
    return when (type) {
        SelectableProductType.Meal -> {
            MealSelectedProduct(
                mealPrice = price,
                meals = 1,
                name = name
            )
        }

        SelectableProductType.Bottle -> {
            BottleSelectedProduct(
                bottlePrice = price,
                bottles = 1,
                name = name
            )
        }
    }
}

fun MealSelectableProduct(name: String, price: Double): SelectableProduct {
    return SelectableProduct(
        name = name,
        price = Price(price),
        type = SelectableProductType.Meal
    )
}

fun BottleSelectableProduct(name: String, price: Double): SelectableProduct {
    return SelectableProduct(
        name = name,
        price = Price(price),
        type = SelectableProductType.Bottle
    )
}