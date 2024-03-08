package dev.xinto.cashier.common.domain.model

sealed interface ProductDiff {

    val id: String

    data class Remove(override val id: String) : ProductDiff

    data class Add(
        override val id: String,
        val name: String,
        val price: Price,
        val type: ProductType
    ) : ProductDiff

    data class Edit(
        override val id: String,
        val name: String,
        val price: Price,
        val type: ProductType
    ) : ProductDiff

}