package com.xinto.cashier.db.converter

import androidx.room.TypeConverter
import com.xinto.cashier.db.entity.EntityPaymentType
import com.xinto.cashier.db.entity.EntityProductType

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