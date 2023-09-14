package com.xinto.cashier.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

fun Double.asPrice() = Price(this)

fun <T> Iterable<T>.priceOf(selector: (T) -> Price): Price {
    var price = Price.Zero
    for (element in this) {
        price += selector(element)
    }
    return price
}

@JvmInline
@Parcelize
value class Price(val value: Double) : Parcelable, Comparable<Price> {

    override fun compareTo(other: Price): Int {
        return this.value.compareTo(other.value)
    }

    companion object {
        val Zero = Price(0.0)
    }

    override fun toString(): String {
        return String.format("%.2fლ", value)
    }

    operator fun plus(other: Price): Price {
        return Price(this.value + other.value)
    }

    operator fun minus(other: Price): Price {
        return Price(this.value - other.value)
    }

    operator fun minus(other: Double): Price {
        return Price(this.value - other)
    }

    operator fun times(other: Price): Price {
        return Price(this.value * other.value)
    }

    operator fun times(other: Double): Price {
        return Price(this.value * other)
    }

    operator fun times(other: Int): Price {
        return Price(this.value * other)
    }
}