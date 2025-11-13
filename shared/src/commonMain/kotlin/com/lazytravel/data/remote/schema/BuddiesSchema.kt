package com.lazytravel.data.remote.schema

/**
 * Travel Buddies / Find Travel Companions Schemas
 *
 * For users to find others to travel with
 */

/**
 * Buddy Requests - Find travel companions
 */
val buddyRequestsSchema = collectionSchema {
    name = "buddy_requests"
    type = CollectionType.BASE

    fields {
        relation("created_by") {
            collectionId = "users"
            maxSelect = 1
            required = true
        }

        text("trip_name") {
            required = true
            min = 2
            max = 200
        }

        relation("destination") {
            collectionId = "destinations"
            maxSelect = 1
            required = true
        }

        date("start_date") {
            required = true
        }

        date("end_date") {
            required = true
        }

        number("total_slots") {
            required = true
            min = 2.0
            onlyInt = true
        }

        number("filled_slots") {
            required = true
            min = 1.0  // At least creator
            onlyInt = true
        }

        number("price_per_person") {
            required = true
            min = 0.0
        }

        editor("description") {
            required = true
        }

        text("requirements") {
            required = false
            max = 500
        }

        select("status") {
            values = listOf("open", "full", "closed")
            maxSelect = 1
            required = true
        }

        // Creator's rating (for display)
        number("creator_rating") {
            required = false
            min = 0.0
            max = 5.0
        }

        // Creator's total reviews
        number("creator_reviews_count") {
            required = false
            min = 0.0
            onlyInt = true
        }

        bool("is_verified") {
            required = true
        }
    }

    indexes {
        index("destination")
        index("status")
        index("start_date")
        compositeIndex("status", "start_date")
        compositeIndex("destination", "status")
    }

    listRule = "status = 'open'"
    viewRule = null
    createRule = "@request.auth.id != ''"
    updateRule = "@request.auth.id = created_by.id"
    deleteRule = "@request.auth.id = created_by.id"
}

/**
 * Buddy Request Tags - Tags for buddy requests
 */
val buddyRequestTagsSchema = collectionSchema {
    name = "buddy_request_tags"
    type = CollectionType.BASE

    fields {
        relation("buddy_request") {
            collectionId = "buddy_requests"
            maxSelect = 1
            required = true
            cascadeDelete = true
        }

        text("tag") {
            required = true
            min = 2
            max = 50
        }
    }

    indexes {
        index("buddy_request")
        index("tag")
        compositeIndex("buddy_request", "tag")
    }

    listRule = null
    viewRule = null
    createRule = "@request.auth.id != ''"
    updateRule = "@request.auth.id = buddy_request.created_by.id"
    deleteRule = "@request.auth.id = buddy_request.created_by.id"
}
