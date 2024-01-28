package dev.xinto.cashier.common.domain.model

fun Iterable<StatusProduct>.price() = priceOf { it.price }

sealed interface StatusProduct {
    val name: String
    val price: Price
    val countAsString: String
}

data class MealStatusProduct(
    override val name: String,
    val mealPrice: Price,
    val meals: Int,
) : StatusProduct {
    override val price = mealPrice * meals
    override val countAsString: String = meals.toString()
}

data class BottleStatusProduct(
    override val name: String,
    val bottlePrice: Price,
    val bottles: Int,
) : StatusProduct {
    override val price = bottlePrice * bottles
    override val countAsString: String = bottles.toString()
}