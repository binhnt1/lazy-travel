package com.lazytravel.data.remote.schema

/**
 * Notifications Schema
 *
 * For app-wide notifications to users
 */

/**
 * Notifications - System notifications for users
 */
val notificationsSchema = collectionSchema {
    name = "notifications"
    type = CollectionType.BASE

    fields {
        relation("user") {
            collectionId = "users"
            maxSelect = 1
            required = true
            cascadeDelete = true
        }

        select("type") {
            values = listOf(
                "vote_request",
                "trip_invitation",
                "vote_completed",
                "trip_update",
                "comment_reply",
                "post_like",
                "message",
                "reminder",
                "achievement_unlocked"
            )
            maxSelect = 1
            required = true
        }

        text("title") {
            required = true
            max = 200
        }

        text("message") {
            required = true
            max = 500
        }

        text("action_url") {
            required = false
            max = 500
        }

        relation("related_trip") {
            collectionId = "trips"
            maxSelect = 1
            required = false
        }

        relation("related_post") {
            collectionId = "posts"
            maxSelect = 1
            required = false
        }

        select("priority") {
            values = listOf("normal", "high", "urgent")
            maxSelect = 1
            required = true
        }

        bool("is_read") {
            required = true
        }
    }

    indexes {
        index("user")
        index("is_read")
        index("priority")
        index("created")
        compositeIndex("user", "is_read")
        compositeIndex("user", "created")
    }

    listRule = "user.id = @request.auth.id"
    viewRule = "user.id = @request.auth.id"
    createRule = ""  // System only
    updateRule = "user.id = @request.auth.id"  // Can mark as read
    deleteRule = "user.id = @request.auth.id"
}
