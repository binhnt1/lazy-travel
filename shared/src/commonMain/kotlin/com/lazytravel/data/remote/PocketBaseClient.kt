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
 *
 * Supports dual authentication:
 * - Admin token: For creating/updating collections (superuser operations)
 * - Collection token: For CRUD operations on records
 */
object PocketBaseClient {

    private var httpClient: HttpClient? = null

    /**
     * Admin auth token - for collection management operations
     * Used for: create collection, update schema, delete collection
     */
    var adminToken: String? = null
        private set

    /**
     * Collection auth token - for record CRUD operations
     * Used for: create/read/update/delete records in collections
     */
    var collectionToken: String? = null
        private set

    /**
     * Backward compatibility - returns admin token by default
     */
    @Deprecated("Use adminToken or collectionToken directly", ReplaceWith("adminToken"))
    var authToken: String?
        get() = adminToken
        private set(value) { adminToken = value }

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
     * Set admin auth token (after admin login)
     * Use for collection management operations
     */
    fun setAdminToken(token: String) {
        adminToken = token
        println("🔐 Admin token set")
    }

    /**
     * Set collection auth token (after collection user login)
     * Use for record CRUD operations
     */
    fun setCollectionToken(token: String) {
        collectionToken = token
        println("🔐 Collection token set")
    }

    /**
     * Backward compatibility - sets admin token
     */
    @Deprecated("Use setAdminToken() or setCollectionToken()", ReplaceWith("setAdminToken(token)"))
    fun setAuthToken(token: String) {
        setAdminToken(token)
    }

    /**
     * Clear admin token
     */
    fun clearAdminToken() {
        adminToken = null
        println("🔓 Admin token cleared")
    }

    /**
     * Clear collection token
     */
    fun clearCollectionToken() {
        collectionToken = null
        println("🔓 Collection token cleared")
    }

    /**
     * Clear all auth tokens
     */
    fun clearAllTokens() {
        adminToken = null
        collectionToken = null
        println("🔓 All tokens cleared")
    }

    /**
     * Backward compatibility
     */
    @Deprecated("Use clearAdminToken() or clearAllTokens()", ReplaceWith("clearAdminToken()"))
    fun clearAuthToken() {
        clearAdminToken()
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
                // DO NOT add auth header here
                // Each API call will add the appropriate token (admin or collection)
            }
        }
    }

    /**
     * Close client (cleanup)
     */
    fun close() {
        httpClient?.close()
        httpClient = null
        clearAllTokens()
    }
}
