package com.xinto.cashier.network.registry

import android.content.Context
import com.xinto.cashier.network.registry.model.ApiProduct
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.File

interface RegistryApi {

    suspend fun getProducts(forceRefresh: Boolean): List<ApiProduct>

}

class DefaultRegistryApi(context: Context) : RegistryApi {

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        defaultRequest {
            header("accept-encoding", "application/json")
        }
    }
    private val file = File(context.filesDir, "registry.json")

    override suspend fun getProducts(forceRefresh: Boolean): List<ApiProduct> {
        return withContext(Dispatchers.IO) {
            if (forceRefresh || !file.exists()) {
                file.delete()
                file.createNewFile()

                val response = client.get("https://raw.githubusercontent.com/X1nto/Cashier/master/registry.json")
                file.writeText(response.bodyAsText())
//                return@withContext response.body()
            }

            Json.decodeFromStream(file.inputStream())
        }
    }
}