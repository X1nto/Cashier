package com.xinto.cashier.domain.repository

import com.xinto.cashier.db.entity.EntityProduct
import com.xinto.cashier.db.entity.EntityPaymentType
import com.xinto.cashier.db.entity.EntityProductType
import com.xinto.cashier.db.store.ProductStoreImpl
import com.xinto.cashier.domain.model.*

interface RegistryRepository {

    suspend fun getProducts(): List<SelectableProduct>

    suspend fun payWithCash(selectedProducts: List<SelectedProduct>)
    suspend fun payWithCard(selectedProducts: List<SelectedProduct>)

}

object RegistryRepositoryImpl : RegistryRepository {

    override suspend fun getProducts(): List<SelectableProduct> {
        return listOf(
            MealSelectableProduct(name = "ხაჭაპური იმერული", price = 4.0),
            MealSelectableProduct(name = "ხაჭაპური ნახ ფენოვანი", price = 3.0),
            MealSelectableProduct(name = "ხაჭაპური აჭარული", price = 10.0),
            MealSelectableProduct(name = "ლობიანი იმერული", price = 3.0),
            MealSelectableProduct(name = "ლობიანი ნახ ფენოვანი", price = 2.5),
            MealSelectableProduct(name = "ლობიანი ლორით", price = 3.40),
            MealSelectableProduct(name = "კუბდარი", price = 4.5),
            MealSelectableProduct(name = "ხაბიზგინა", price = 2.5),
            MealSelectableProduct(name = "ფიჩინი ხორცით", price = 7.0),
            MealSelectableProduct(name = "ფიჩინი სოკოთი", price = 5.0),
            MealSelectableProduct(name = "ღვეზელი ქათმით", price = 2.8),
            MealSelectableProduct(name = "ღვეზელი სოკოთი", price = 1.8),
            MealSelectableProduct(name = "ღვეზელი კარტოფილით", price = 1.5),
            MealSelectableProduct(name = "ჰოთდოგი", price = 2.0),
            MealSelectableProduct(name = "პიცა", price = 2.5),
            MealSelectableProduct(name = "ნამცხვარი ვაშლის დარიჩინით", price = 2.5),
            MealSelectableProduct(name = "ნამცხვარი ფორთოხლის", price = 2.0),
            MealSelectableProduct(name = "მაფინი", price = 1.5),
            MealSelectableProduct(name = "ქადა", price = 1.5),
            MealSelectableProduct(name = "ფუნთუშა", price = 1.0),
            MealSelectableProduct(name = "ფუნთუშა ჯემით", price = 1.2),
            MealSelectableProduct(name = "ფუნთუშა ხაჭოთი", price = 1.4),
            MealSelectableProduct(name = "მხლოვანი", price = 3.80),
            MealSelectableProduct(name = "მხლოვანი სამარხვო", price = 3.0),
            MealSelectableProduct(name = "განსხვავებული", price = 0.5),
            BottleSelectableProduct(name = "წყალი ბაკურიანი", price = 0.8),
            BottleSelectableProduct(name = "წყალი მთის", price = 0.6),
            BottleSelectableProduct(name = "ბორჯომი", price = 2.0),
            BottleSelectableProduct(name = "ლიკანი", price = 1.5),
            BottleSelectableProduct(name = "ყავა", price = 1.0),
            BottleSelectableProduct(name = "ჩაი", price = 1.0),
            BottleSelectableProduct(name = "ცივი ჩაი", price = 2.0),
            BottleSelectableProduct(name = "კოკა კოლა", price = 1.8),
            BottleSelectableProduct(name = "ყავა კოლა", price = 2.0),
            BottleSelectableProduct(name = "ფანტა", price = 1.8),
            BottleSelectableProduct(name = "სპრაიტი", price = 1.8),
        )
    }

    override suspend fun payWithCard(selectedProducts: List<SelectedProduct>) {
        storeProducts(selectedProducts, EntityPaymentType.Card)
    }

    override suspend fun payWithCash(selectedProducts: List<SelectedProduct>) {
        storeProducts(selectedProducts, EntityPaymentType.Cash)
    }

    private suspend fun storeProducts(selectedProducts: List<SelectedProduct>, entityPaymentType: EntityPaymentType) {
        ProductStoreImpl.putDailyProducts(
            selectedProducts.map {
                it.toEntityProduct(entityPaymentType)
            }
        )
    }

    private fun SelectedProduct.toEntityProduct(entityPaymentType: EntityPaymentType): EntityProduct {
        return EntityProduct(
            id = name + entityPaymentType.toString(),
            name = name,
            price = when (this) {
                is BottleSelectedProduct -> bottlePrice
                is MealSelectedProduct -> mealPrice
                is MeasuredSelectedProduct -> pricePerKilo
            }.price,
            quantity = when (this) {
                is BottleSelectedProduct -> bottles
                is MealSelectedProduct -> meals
                is MeasuredSelectedProduct -> kilos.toInt()
            },
            entityPaymentType = entityPaymentType,
            entityProductType = when (this) {
                is BottleSelectedProduct -> EntityProductType.Bottle
                is MealSelectedProduct -> EntityProductType.Meal
                is MeasuredSelectedProduct -> EntityProductType.Measured
            }
        )
    }
}