package com.xinto.cashier.network.registry

import android.content.Context
import com.xinto.cashier.network.registry.model.ApiProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

interface RegistryApi {

    suspend fun downloadProducts()

    suspend fun parseProducts(): List<ApiProduct>

}

class DefaultRegistryApi(context: Context) : RegistryApi {

    private val file = File(context.filesDir, "registry.json")

    override suspend fun downloadProducts() {
        withContext(Dispatchers.IO) {
            val url = URL("https://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@raw.githubusercontent.com/X1nto/Cashier/master/registry.json")
            val connection = url.openConnection() as HttpURLConnection
            try {
                val inputStream = connection.inputStream
                val file = file.apply {
                    delete()
                    createNewFile()
                }
                val outputStream = FileOutputStream(file)
                var bytesRead: Int
                val buffer = ByteArray(4096)
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }
            } finally {
                connection.disconnect()
            }
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun parseProducts(): List<ApiProduct> {
        if (!file.exists()) {
            downloadProducts()
        }

        return Json.decodeFromStream(file.inputStream())
    }

}