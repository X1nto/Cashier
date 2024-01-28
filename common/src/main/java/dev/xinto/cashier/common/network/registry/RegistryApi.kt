package dev.xinto.cashier.common.network.registry

import android.content.Context
import dev.xinto.cashier.common.network.TlsCompatSocketFactory
import dev.xinto.cashier.common.network.registry.model.ApiProduct
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.File
import java.net.UnknownHostException

interface RegistryApi {

    suspend fun getProducts(forceRefresh: Boolean): List<ApiProduct>

}

class DefaultRegistryApi(context: Context) : RegistryApi {

    private val client = HttpClient(Android) {
        engine {
            sslManager = {
                it.sslSocketFactory = TlsCompatSocketFactory()
            }
        }
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        defaultRequest {
            header("accept-encoding", "application/json")
        }
    }
    private val file = File(context.filesDir, "registry.json")

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getProducts(forceRefresh: Boolean): List<ApiProduct> {
        return withContext(Dispatchers.IO) {
            if (forceRefresh || !file.exists()) {
                try {
                    val response =
                        client.get("https://raw.githubusercontent.com/X1nto/Cashier/master/registry.json")
                    if (response.status.isSuccess()) {
                        file.delete()
                        file.createNewFile()
                        file.writeText(response.bodyAsText())
                    }
                } catch (_: UnknownHostException) {
                }
            }

            Json.decodeFromStream(file.inputStream())
        }
    }
}