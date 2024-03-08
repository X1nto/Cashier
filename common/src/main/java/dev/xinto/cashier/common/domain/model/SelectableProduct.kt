package dev.xinto.cashier.common.domain.model


data class SelectableProduct(
    val id: String,
    val name: String,
    val price: Price,
    val type: ProductType
)


fun MealSelectableProduct(id: String, name: String, price: Int): SelectableProduct {
    return SelectableProduct(
        id = id,
        name = name,
        price = Price(price),
        type = ProductType.Meal
    )
}

fun BottleSelectableProduct(
    id: String,
    name: String,
    price: Int
): SelectableProduct {
    return SelectableProduct(
        id = id,
        name = name,
        price = Price(price),
        type = ProductType.Bottle
    )
}