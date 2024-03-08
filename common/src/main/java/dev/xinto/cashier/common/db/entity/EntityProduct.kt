package dev.xinto.cashier.common.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class EntityPaymentType {
    Card, Cash
}

enum class EntityProductType {
    Meal, Bottle, Measured
}

@Entity(tableName = "products")
data class EntityProduct(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "price")
    val price: Int,

    @ColumnInfo(name = "quantity")
    val quantity: Int,

    @ColumnInfo(name = "payment")
    val entityPaymentType: EntityPaymentType,

    @ColumnInfo(name = "type")
    val entityProductType: EntityProductType
)