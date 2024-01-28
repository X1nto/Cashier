package dev.xinto.cashier.common.db.converter

import androidx.room.TypeConverter
import dev.xinto.cashier.common.db.entity.EntityPaymentType
import dev.xinto.cashier.common.db.entity.EntityProductType

class ProductConverters {

    @TypeConverter
    fun convertPaymentToString(value: EntityPaymentType): String {
        return value.name
    }

    @TypeConverter
    fun convertStringToPayment(value: String): EntityPaymentType {
        return EntityPaymentType.valueOf(value)
    }

    @TypeConverter
    fun convertProductToString(value: EntityProductType): String {
        return value.name
    }

    @TypeConverter
    fun convertStringToProduct(value: String): EntityProductType {
        return EntityProductType.valueOf(value)
    }

}