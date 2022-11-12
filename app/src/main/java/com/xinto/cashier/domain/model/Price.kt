package com.xinto.cashier.domain.model

import androidx.compose.runtime.Immutable

fun Double.toPrice() = Price(this)

fun <T> Iterable<T>.priceOf(selector: (T) -> Price): Price {
    var price = Price.Zero
    for (element in this) {
        price += selector(element)
    }
    return price
}

@JvmInline
@Immutable
value class Price(val price: Double) {

    companion object {
        val Zero = Price(0.0)
    }

    override fun toString(): String {
        return String.format("%.2fლ", price)
    }

    operator fun plus(other: Price): Price {
        return Price(this.price + other.price)
    }

    operator fun times(other: Price): Price {
        return Price(this.price * other.price)
    }

    operator fun times(other: Double): Price {
        return Price(this.price * other)
    }

    operator fun times(other: Int): Price {
        return Price(this.price * other)
    }
}