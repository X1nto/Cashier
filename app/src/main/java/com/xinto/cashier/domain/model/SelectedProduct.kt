package com.xinto.cashier.domain.model

import android.os.Parcel
import android.os.Parcelable

fun Iterable<SelectedProduct>.price() = priceOf { it.price }

@JvmInline
value class ParcelableSelectedProduct(
    val product: SelectedProduct
) : Parcelable {

    enum class Type {
        Bottle, Food, Measured
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        when (product) {
            is MealSelectedProduct -> {
                dest.writeSerializable(Type.Food)
                dest.writeDouble(product.mealPrice.value)
                dest.writeInt(product.meals)
                dest.writeString(product.name)
            }
            is BottleSelectedProduct -> {
                dest.writeSerializable(Type.Bottle)
                dest.writeDouble(product.bottlePrice.value)
                dest.writeInt(product.bottles)
                dest.writeString(product.name)
            }
            is MeasuredSelectedProduct -> {
                dest.writeSerializable(Type.Measured)
                dest.writeDouble(product.pricePerKilo.value)
                dest.writeDouble(product.kilos)
                dest.writeString(product.name)
            }
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParcelableSelectedProduct> {
        override fun createFromParcel(parcel: Parcel): ParcelableSelectedProduct {
            val selectedProduct = when (Type.valueOf(parcel.readString()!!)) {
                Type.Measured -> {
                    MeasuredSelectedProduct(
                        pricePerKilo = parcel.readDouble().asPrice(),
                        kilos = parcel.readDouble(),
                        name = parcel.readString()!!
                    )
                }
                Type.Food -> {
                    MealSelectedProduct(
                        mealPrice = parcel.readDouble().asPrice(),
                        meals = parcel.readInt(),
                        name = parcel.readString()!!
                    )
                }
                Type.Bottle -> {
                    BottleSelectedProduct(
                        bottlePrice = parcel.readDouble().asPrice(),
                        bottles = parcel.readInt(),
                        name = parcel.readString()!!
                    )
                }
            }
            return ParcelableSelectedProduct(selectedProduct)
        }

        override fun newArray(size: Int): Array<ParcelableSelectedProduct?> {
            return arrayOfNulls(size)
        }
    }
}

sealed interface SelectedProduct {

    val price: Price
    val name: String

    val countAsString: String
    fun parseNewCount(count: String): SelectedProduct?

    val canDecrease: Boolean

    fun increased(): SelectedProduct
    fun decreased(): SelectedProduct?
}

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