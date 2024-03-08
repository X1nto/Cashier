package dev.xinto.cashier.common.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductItem(
    val id: String,
    val name: String,
    val price: Price,
    val type: ProductType,
    val status: ProductItemStatus
) : Parcelable

enum class ProductItemStatus {
    Existing,
    New,
    Removed,
    Edited
}