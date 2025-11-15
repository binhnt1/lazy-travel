package com.lazytravel.data.remote.schema

import com.lazytravel.data.remote.PocketBaseConfig

/**
 * Features collection schema
 * For landing page feature cards
 */
val featuresSchema = collectionSchema {
    name = PocketBaseConfig.Collections.FEATURES
    type = CollectionType.BASE

    fields {
        // Icon emoji
        text("icon") {
            required = true
            min = 1
            max = 10
        }

        // Title translation key
        text("title") {
            required = true
            min = 1
            max = 100
        }

        // Description translation key
        text("description") {
            required = true
            min = 1
            max = 200
        }

        // Display order
        number("order") {
            required = true
            min = 0.0
            onlyInt = true
        }

        // Active status (default true)
        bool("active")
    }

    // Public read access
    listRule = ""
    viewRule = ""

    // No public write access (admin only)
    createRule = null
    updateRule = null
    deleteRule = null
}