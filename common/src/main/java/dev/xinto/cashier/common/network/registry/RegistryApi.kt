package dev.xinto.cashier.common.network.registry

import android.content.Context
import dev.xinto.cashier.common.BuildConfig
import dev.xinto.cashier.common.network.TlsCompatSocketFactory
import dev.xinto.cashier.common.network.registry.model.ApiProduct
import dev.xinto.cashier.common.network.registry.model.ApiProductPatches
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.File
import java.net.UnknownHostException

class RegistryApi(context: Context)  {

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
    suspend fun getProducts(forceRefresh: Boolean): List<ApiProduct> {
        return withContext(Dispatchers.IO) {
            if (!file.exists()) {
                try {
                    val response = client.get(getProductsUrl())
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

    suspend fun updateProducts(patches: ApiProductPatches): Boolean {
        return withContext(Dispatchers.IO) {
            client.post(getProductsUrl()) {
                contentType(ContentType.Application.Json)
                setBody(patches)
            }.status.isSuccess()
        }
    }

    companion object {
        fun getProductsUrl(): String {
            return BuildConfig.BACKEND_URL + "/products"
        }
    }
}