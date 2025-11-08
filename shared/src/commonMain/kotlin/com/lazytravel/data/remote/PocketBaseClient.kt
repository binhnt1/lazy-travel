package com.lazytravel.data.remote

import io.github.agrevster.pocketbaseKotlin.PocketbaseClient

/**
 * PocketBase Client Singleton
 * Shared across the app
 */
object PocketBaseClient {

    private var client: PocketbaseClient? = null

    /**
     * Initialize PocketBase client
     * Call this once during app startup
     */
    fun initialize() {
        if (client == null) {
            client = PocketbaseClient(PocketBaseConfig.BASE_URL)
            println("✅ PocketBase initialized: ${PocketBaseConfig.BASE_URL}")
        }
    }

    /**
     * Get the PocketBase client instance
     */
    fun getInstance(): PocketbaseClient {
        return client ?: throw IllegalStateException(
            "PocketBase client not initialized! Call PocketBaseClient.initialize() first."
        )
    }

    /**
     * Update base URL (useful when switching from IP to domain)
     */
    fun updateBaseUrl(newUrl: String) {
        client = PocketbaseClient(newUrl)
        println("✅ PocketBase URL updated to: $newUrl")
    }
}
