package com.xinto.cashier.domain.model

import androidx.compose.runtime.Stable

fun Iterable<SelectedProduct>.price() = priceOf { it.price }

@Stable
sealed interface SelectedProduct {

    val price: Price
    val name: String

    val countAsString: String
    fun parseNewCount(count: String): SelectedProduct?

    val canDecrease: Boolean

    fun increased(): SelectedProduct
    fun decreased(): SelectedProduct?
}

@Stable
data class MealSelectedProduct(
    val mealPrice: Price,
    val meals: Int,
    override val name: String
) : SelectedProduct {
    override val price = mealPrice * meals
    override val countAsString: String = meals.toString()

    override fun parseNewCount(count: String): SelectedProduct? {
        val parsedCount = count.toIntOrNull() ?: return null
        if (parsedCount <= 0) return null
        return this.copy(meals = parsedCount)
    }

    override val canDecrease: Boolean = meals - 1 > 0

    override fun increased(): SelectedProduct {
        return this.copy(meals = meals + 1)
    }

    override fun decreased(): SelectedProduct? {
        val nextItemCount = meals - 1
        if (nextItemCount <= 0) {
            return null
        }
        return this.copy(meals = nextItemCount)
    }
}

@Stable
data class BottleSelectedProduct(
    val bottlePrice: Price,
    val bottles: Int,
    override val name: String
) : SelectedProduct {
    override val price = bottlePrice * bottles
    override val countAsString: String = bottles.toString()

    override fun parseNewCount(count: String): SelectedProduct? {
        val parsedCount = count.toIntOrNull() ?: return null
        if (parsedCount <= 0) return null
        return this.copy(bottles = parsedCount)
    }

    override val canDecrease: Boolean = bottles - 1 > 0

    override fun increased(): SelectedProduct {
        return this.copy(bottles = bottles + 1)
    }

    override fun decreased(): SelectedProduct? {
        val nextItemCount = bottles - 1
        if (nextItemCount <= 0) {
            return null
        }
        return this.copy(bottles = nextItemCount)
    }
}

@Stable
data class MeasuredSelectedProduct(
    val pricePerKilo: Price,
    val kilos: Double,
    override val name: String
) : SelectedProduct {
    override val price = pricePerKilo * kilos
    override val countAsString: String = kilos.toString()

    override fun parseNewCount(count: String): SelectedProduct? {
        val parsedCount = count.toDoubleOrNull() ?: return null
        if (parsedCount <= 0) return null
        return this.copy(kilos = parsedCount)
    }

    override val canDecrease: Boolean = kilos - 0.1 > 0

    override fun increased(): SelectedProduct {
        return this.copy(kilos = kilos + 1.0)
    }

    override fun decreased(): SelectedProduct? {
        val nextKilos = kilos - 0.1
        if (nextKilos <= 0) {
            return null
        }
        return this.copy(kilos = nextKilos)
    }
}