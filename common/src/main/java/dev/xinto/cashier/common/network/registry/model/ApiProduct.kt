package dev.xinto.cashier.common.network.registry.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.Serializable

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
            encoder.encodeString(value.name.lowercase())
        }
    }
}
