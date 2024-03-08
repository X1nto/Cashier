package dev.xinto.cashier.common.network.registry.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class ApiProduct(
    val id: String,
    val name: String,
    val price: Int,
    val type: ApiProductType
)

@Serializable(with = ApiProductType.Serializer::class)
enum class ApiProductType {
    Food, Drink;

    companion object Serializer : KSerializer<ApiProductType> {
        override val descriptor: SerialDescriptor
            get() = serialDescriptor<String>()

        override fun deserialize(decoder: Decoder): ApiProductType {
            return when (decoder.decodeString()) {
                "food" -> Food
                "drink" -> Drink
                else -> throw UnsupportedOperationException()
            }
        }

        override fun serialize(encoder: Encoder, value: ApiProductType) {
            encoder.encodeString(value.name.lowercase())
        }
    }
}
