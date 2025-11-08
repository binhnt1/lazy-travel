package com.lazytravel.data.remote

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * PocketBase Client using Ktor
 * Direct REST API implementation
 */
object PocketBaseClient {

    private var httpClient: HttpClient? = null
    var authToken: String? = null
        private set

    /**
     * Initialize Ktor HTTP client
     */
    fun initialize() {
        if (httpClient == null) {
            httpClient = createHttpClient()
            println("✅ PocketBase client initialized: ${PocketBaseConfig.BASE_URL}")
        }
    }

    /**
     * Get HTTP client instance
     */
    fun getClient(): HttpClient {
        return httpClient ?: throw IllegalStateException(
            "PocketBase client not initialized! Call PocketBaseClient.initialize() first."
        )
    }

    /**
     * Update auth token (after admin login)
     */
    fun setAuthToken(token: String) {
        authToken = token
    }

    /**
     * Clear auth token
     */
    fun clearAuthToken() {
        authToken = null
    }

    private fun createHttpClient(): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = true
                })
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("🌐 Ktor: $message")
                    }
                }
                level = LogLevel.INFO
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 30000
                connectTimeoutMillis = 30000
            }

            defaultRequest {
                url(PocketBaseConfig.BASE_URL)
                // Add auth header if available
                authToken?.let { token ->
                    headers.append("Authorization", token)
                }
            }
        }
    }

    /**
     * Close client (cleanup)
     */
    fun close() {
        httpClient?.close()
        httpClient = null
    }
}
