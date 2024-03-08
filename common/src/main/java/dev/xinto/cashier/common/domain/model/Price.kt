package dev.xinto.cashier.common.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

fun Int.asPrice() = Price(this)

inline fun <T> Iterable<T>.priceOf(selector: (T) -> Price): Price {
    var price = Price.Zero
    for (element in this) {
        price += selector(element)
    }
    return price
}

@JvmInline
@Parcelize
value class Price(val value: Int) : Parcelable, Comparable<Price> {

    override fun compareTo(other: Price): Int {
        return this.value.compareTo(other.value)
    }

    companion object {
        val Zero = Price(0)

        fun fromString(price: String): Price? {
            if (price.filter { it.isDigit() }.isBlank()) return null

            val lari: Int?
            val tetri: Int?

            if (price.contains('.')) {
                lari = if (price.startsWith('.')) 0 else {
                    price.substringBefore('.').toIntOrNull()
                }

                var tetriString = price.substringAfter('.')

                if (tetriString.length == 1) {
                    tetriString += '0'
                } else if (tetriString.length >=3) {
                    return null
                }

                tetri = tetriString.toIntOrNull()
            } else {
                lari = price.toIntOrNull()
                tetri = 0
            }

            if (lari == null || tetri == null) return null

            return Price((lari * 100) + tetri)
        }
    }

    override fun toString(): String {
        if (value == 0) return "0.00"

        if (value < 10) return "0.0${value}"

        if (value < 100) return "0.${value}"

        val valueAsString = value.toString()
        val lari = valueAsString.substring(0, valueAsString.lastIndex - 1)
        val tetri = valueAsString.substring(valueAsString.lastIndex - 1, valueAsString.length)

        return "${lari}.${tetri}"
    }

    operator fun plus(other: Price): Price {
        return Price(this.value + other.value)
    }

    operator fun minus(other: Price): Price {
        return Price(this.value - other.value)
    }

    operator fun times(other: Price): Price {
        return Price(this.value * other.value)
    }

    operator fun times(other: Int): Price {
        return Price(this.value * other)
    }
}