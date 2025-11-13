package com.lazytravel.data.remote.schema

/**
 * Trip Photos Schema
 *
 * For shared trip albums and memories
 */

/**
 * Trip Photos - Photos from trips for memories
 */
val tripPhotosSchema = collectionSchema {
    name = "trip_photos"
    type = CollectionType.BASE

    fields {
        relation("trip") {
            collectionId = "trips"
            maxSelect = 1
            required = true
            cascadeDelete = true
        }

        relation("uploaded_by") {
            collectionId = "users"
            maxSelect = 1
            required = true
        }

        file("photo") {
            maxSelect = 1
            maxSize = 10485760  // 10MB
            mimeTypes = listOf(
                "image/jpeg",
                "image/png",
                "image/webp"
            )
            thumbs = listOf(
                "100x100",
                "300x300",
                "600x600",
                "1200x1200"
            )
        }

        text("caption") {
            required = false
            max = 500
        }

        text("location_tagged") {
            required = false
            max = 200
        }

        date("taken_at") {
            required = false
        }

        number("likes_count") {
            required = true
            min = 0.0
            onlyInt = true
        }
    }

    indexes {
        index("trip")
        index("uploaded_by")
        index("taken_at")
        compositeIndex("trip", "taken_at")
    }

    listRule = "@collection.trip_members.trip.id ?= trip.id"
    viewRule = "@collection.trip_members.trip.id ?= trip.id"
    createRule = "@request.auth.id != '' && @collection.trip_members.trip.id ?= trip.id && @collection.trip_members.user.id ?= @request.auth.id"
    updateRule = "@request.auth.id = uploaded_by.id"
    deleteRule = "@request.auth.id = uploaded_by.id || @request.auth.id = trip.created_by.id"
}
