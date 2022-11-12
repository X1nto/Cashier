package com.xinto.cashier.db.entity

enum class EntityPaymentType {
    Card, Cash
}

enum class EntityProductType {
    Meal, Bottle, Measured
}

data class EntityProduct(
    val id: String,
    val name: String,
    val price: Double,
    val quantity: Int,
    val entityPaymentType: EntityPaymentType,
    val entityProductType: EntityProductType
) {
    val isBottle = entityProductType == EntityProductType.Bottle
    val isMeal = entityProductType == EntityProductType.Meal
    val isMeasured = entityProductType == EntityProductType.Measured

    val isPaidCard = entityPaymentType == EntityPaymentType.Card
    val isPaidCash = entityPaymentType == EntityPaymentType.Cash
}