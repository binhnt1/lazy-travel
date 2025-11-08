package com.lazytravel.data.remote

/**
 * PocketBase Configuration
 * Change baseUrl when you have a domain/SSL
 */
object PocketBaseConfig {
    // Current IP address (change to domain later)
    const val BASE_URL = "http://103.159.51.215:8090"

    // Collections
    object Collections {
        const val DESTINATIONS = "destinations"
        const val HOTELS = "hotels"
        const val REVIEWS = "reviews"
        // Add more collections here as needed
    }

    // Admin credentials for auto collection setup
    // TODO: Move to secure storage in production!
    object Admin {
        const val EMAIL = "binhnt1@gmail.com"
        const val PASSWORD = "A1a@a#a\$a%"
    }
}
