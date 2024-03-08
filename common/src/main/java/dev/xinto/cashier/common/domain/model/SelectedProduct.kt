package dev.xinto.cashier.common.domain.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

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
                dest.writeString(Type.Food.name)
                dest.writeInt(product.mealPrice.value)
                dest.writeInt(product.meals)
                dest.writeString(product.name)
            }
            is BottleSelectedProduct -> {
                dest.writeString(Type.Bottle.name)
                dest.writeInt(product.bottlePrice.value)
                dest.writeInt(product.bottles)
                dest.writeString(product.name)
            }
            is MeasuredSelectedProduct -> {
                dest.writeString(Type.Measured.name)
                dest.writeInt(product.pricePerGram.value)
                dest.writeInt(product.grams)
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
                        pricePerGram = parcel.readInt().asPrice(),
                        grams = parcel.readInt(),
                        name = parcel.readString()!!
                    )
                }
                Type.Food -> {
                    MealSelectedProduct(
                        mealPrice = parcel.readInt().asPrice(),
                        meals = parcel.readInt(),
                        name = parcel.readString()!!
                    )
                }
                Type.Bottle -> {
                    BottleSelectedProduct(
                        bottlePrice = parcel.readInt().asPrice(),
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

sealed interface SelectedProduct : Parcelable {

    val price: Price
    val name: String

    val countAsString: String
    fun parseNewCount(count: String): SelectedProduct?

    val canDecrease: Boolean

    fun increased(): SelectedProduct
    fun decreased(): SelectedProduct?
}

@Parcelize
data class MealSelectedProduct(
    val mealPrice: Price,
    val meals: Int,
    override val name: String
) : SelectedProduct {

    @IgnoredOnParcel
    override val price = mealPrice * meals

    @IgnoredOnParcel
    override val countAsString: String = meals.toString()

    override fun parseNewCount(count: String): SelectedProduct? {
        val parsedCount = count.toIntOrNull() ?: return null
        if (parsedCount <= 0) return null
        return this.copy(meals = parsedCount)
    }

    @IgnoredOnParcel
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

@Parcelize
data class BottleSelectedProduct(
    val bottlePrice: Price,
    val bottles: Int,
    override val name: String
) : SelectedProduct {
    @IgnoredOnParcel
    override val price = bottlePrice * bottles

    @IgnoredOnParcel
    override val countAsString: String = bottles.toString()

    override fun parseNewCount(count: String): SelectedProduct? {
        val parsedCount = count.toIntOrNull() ?: return null
        if (parsedCount <= 0) return null
        return this.copy(bottles = parsedCount)
    }

    @IgnoredOnParcel
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

@Parcelize
data class MeasuredSelectedProduct(
    val pricePerGram: Price,
    val grams: Int,
    override val name: String
) : SelectedProduct {

    @IgnoredOnParcel
    override val price = pricePerGram * grams

    @IgnoredOnParcel
    override val countAsString: String = grams.toString()

    override fun parseNewCount(count: String): SelectedProduct? {
        val parsedCount = count.toIntOrNull() ?: return null
        if (parsedCount <= 0) return null
        return this.copy(grams = parsedCount)
    }


    @IgnoredOnParcel
    override val canDecrease: Boolean = grams - 0.1 > 0

    override fun increased(): SelectedProduct {
        return this.copy(grams = grams + 1000)
    }

    override fun decreased(): SelectedProduct? {
        val nextGrams = grams - 100
        if (nextGrams <= 0) {
            return null
        }
        return this.copy(grams = nextGrams)
    }
}