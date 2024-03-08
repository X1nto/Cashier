package dev.xinto.cashier.common.domain.model

data class Product(
    val id: String,
    val name: String,
    val price: Price,
    val type: ProductType
)

enum class ProductType {
    Meal,
    Bottle
}

fun Product.toSelectedProduct(): SelectedProduct {
    return when (type) {
        ProductType.Meal -> {
            MealSelectedProduct(
                mealPrice = price,
                meals = 1,
                name = name
            )
        }

        ProductType.Bottle -> {
            BottleSelectedProduct(
                bottlePrice = price,
                bottles = 1,
                name = name
            )
        }
    }
}