package com.xinto.cashier.network.registry.model

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class ApiProduct(
    val name: String,
    val price: String,
    val type: ApiProductType
)

@Serializable(with = ApiProductType.Serializer::class)
enum class ApiProductType {
    Meal, Bottle;

    companion object Serializer : KSerializer<ApiProductType> {
        override val descriptor: SerialDescriptor
            get() = serialDescriptor<String>()

        override fun deserialize(decoder: Decoder): ApiProductType {
            return when (decoder.decodeString()) {
                "meal" -> Meal
                "bottle" -> Bottle
                else -> throw UnsupportedOperationException()
            }
        }

        override fun serialize(encoder: Encoder, value: ApiProductType) {
            encoder.encodeString(value.name.toLowerCase(Locale.current))
        }
    }
}
