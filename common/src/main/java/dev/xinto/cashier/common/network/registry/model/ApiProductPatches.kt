package dev.xinto.cashier.common.network.registry.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiProductPatches(
    val remove: List<String>,
    val edit: List<ApiProduct>,
    val add: List<ApiProduct>
)