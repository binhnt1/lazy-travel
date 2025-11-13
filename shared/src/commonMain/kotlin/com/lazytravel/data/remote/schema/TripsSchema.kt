package com.lazytravel.data.remote.schema

/**
 * Trip Management Schemas
 *
 * Core workflow: Create Trip → Invite Members → Vote Destination → Plan Schedule → Track Expenses
 */

/**
 * Trips - Main trip entity
 */
val tripsSchema = collectionSchema {
    name = "trips"
    type = CollectionType.BASE

    fields {
        text("name") {
            required = true
            min = 2
            max = 200
        }

        relation("destination") {
            collectionId = "destinations"
            maxSelect = 1
            required = false  // Set after voting
        }

        date("start_date") {
            required = true
        }

        date("end_date") {
            required = true
        }

        select("status") {
            values = listOf(
                "planning",
                "voting",
                "vote_completed",
                "confirmed",
                "upcoming",
                "ongoing",
                "completed",
                "cancelled"
            )
            maxSelect = 1
            required = true
        }

        number("budget_per_person") {
            required = false
            min = 0.0
        }

        number("budget_total") {
            required = false
            min = 0.0
        }

        url("cover_image_url") {
            required = false
        }

        relation("created_by") {
            collectionId = "users"
            maxSelect = 1
            required = true
        }

        // Weather info cache (JSON)
        json("weather_info") {
            required = false
        }

        // Flight info (JSON: {from, to, date, time, flight_number})
        json("flight_info") {
            required = false
        }

        date("vote_deadline") {
            required = false
        }
    }

    indexes {
        index("status")
        index("start_date")
        index("created_by")
        compositeIndex("status", "start_date")
    }

    listRule = null
    viewRule = null
    createRule = "@request.auth.id != ''"
    updateRule = "@request.auth.id = created_by.id || @collection.trip_members.user.id ?= @request.auth.id"
    deleteRule = "@request.auth.id = created_by.id"
}

/**
 * Trip Members - Who's in the trip
 */
val tripMembersSchema = collectionSchema {
    name = "trip_members"
    type = CollectionType.BASE

    fields {
        relation("trip") {
            collectionId = "trips"
            maxSelect = 1
            required = true
            cascadeDelete = true
        }

        relation("user") {
            collectionId = "users"
            maxSelect = 1
            required = true
        }

        select("role") {
            values = listOf("organizer", "member")
            maxSelect = 1
            required = true
        }

        select("status") {
            values = listOf("invited", "joined", "declined")
            maxSelect = 1
            required = true
        }

        date("joined_at") {
            required = false
        }
    }

    indexes {
        compositeIndex("trip", "user")  // Unique combination
        index("status")
    }

    listRule = null
    viewRule = null
    createRule = "@request.auth.id != ''"
    updateRule = "@request.auth.id = user.id || @request.auth.id = trip.created_by.id"
    deleteRule = "@request.auth.id = trip.created_by.id"
}

/**
 * Trip Expenses - Money tracking
 */
val tripExpensesSchema = collectionSchema {
    name = "trip_expenses"
    type = CollectionType.BASE

    fields {
        relation("trip") {
            collectionId = "trips"
            maxSelect = 1
            required = true
            cascadeDelete = true
        }

        relation("paid_by") {
            collectionId = "users"
            maxSelect = 1
            required = true
        }

        number("amount") {
            required = true
            min = 0.0
        }

        text("description") {
            required = true
            max = 500
        }

        select("category") {
            values = listOf(
                "food",
                "transport",
                "hotel",
                "activity",
                "shopping",
                "other"
            )
            maxSelect = 1
            required = true
        }

        url("receipt_image_url") {
            required = false
        }

        date("expense_date") {
            required = true
        }

        select("split_type") {
            values = listOf("equal", "custom")
            maxSelect = 1
            required = true
        }

        // Split details (JSON: [{user_id, amount}, ...])
        json("split_data") {
            required = false
        }
    }

    indexes {
        index("trip")
        index("category")
        index("expense_date")
        compositeIndex("trip", "expense_date")
    }

    listRule = "@collection.trip_members.trip.id ?= trip.id && @collection.trip_members.user.id ?= @request.auth.id"
    viewRule = "@collection.trip_members.trip.id ?= trip.id && @collection.trip_members.user.id ?= @request.auth.id"
    createRule = "@request.auth.id != '' && @collection.trip_members.trip.id ?= trip.id && @collection.trip_members.user.id ?= @request.auth.id"
    updateRule = "@request.auth.id = paid_by.id"
    deleteRule = "@request.auth.id = paid_by.id || @request.auth.id = trip.created_by.id"
}

/**
 * Trip Checklists - Organize tasks
 */
val tripChecklistsSchema = collectionSchema {
    name = "trip_checklists"
    type = CollectionType.BASE

    fields {
        relation("trip") {
            collectionId = "trips"
            maxSelect = 1
            required = true
            cascadeDelete = true
        }

        text("name") {
            required = true
            max = 200
        }

        number("total_items") {
            required = true
            min = 0.0
            onlyInt = true
        }

        number("completed_items") {
            required = true
            min = 0.0
            onlyInt = true
        }
    }

    indexes {
        index("trip")
    }

    listRule = null
    viewRule = null
    createRule = "@request.auth.id != ''"
    updateRule = "@collection.trip_members.trip.id ?= trip.id && @collection.trip_members.user.id ?= @request.auth.id"
    deleteRule = "@request.auth.id = trip.created_by.id"
}

/**
 * Trip Checklist Items - Individual tasks
 */
val tripChecklistItemsSchema = collectionSchema {
    name = "trip_checklist_items"
    type = CollectionType.BASE

    fields {
        relation("checklist") {
            collectionId = "trip_checklists"
            maxSelect = 1
            required = true
            cascadeDelete = true
        }

        text("title") {
            required = true
            max = 200
        }

        bool("is_completed") {
            required = true
        }

        relation("assigned_to") {
            collectionId = "users"
            maxSelect = 1
            required = false
        }

        relation("completed_by") {
            collectionId = "users"
            maxSelect = 1
            required = false
        }

        date("completed_at") {
            required = false
        }

        number("order_index") {
            required = true
            onlyInt = true
        }
    }

    indexes {
        index("checklist")
        index("is_completed")
        compositeIndex("checklist", "order_index")
    }

    listRule = null
    viewRule = null
    createRule = "@request.auth.id != ''"
    updateRule = "@request.auth.id != ''"
    deleteRule = "@request.auth.id != ''"
}

/**
 * Trip Notifications - In-trip announcements
 */
val tripNotificationsSchema = collectionSchema {
    name = "trip_notifications"
    type = CollectionType.BASE

    fields {
        relation("trip") {
            collectionId = "trips"
            maxSelect = 1
            required = true
            cascadeDelete = true
        }

        relation("created_by") {
            collectionId = "users"
            maxSelect = 1
            required = true
        }

        text("message") {
            required = true
            max = 500
        }

        select("priority") {
            values = listOf("normal", "important")
            maxSelect = 1
            required = true
        }
    }

    indexes {
        index("trip")
        index("priority")
        compositeIndex("trip", "created")
    }

    listRule = null
    viewRule = null
    createRule = "@request.auth.id != '' && @collection.trip_members.trip.id ?= trip.id && @collection.trip_members.user.id ?= @request.auth.id"
    updateRule = "@request.auth.id = created_by.id"
    deleteRule = "@request.auth.id = created_by.id || @request.auth.id = trip.created_by.id"
}
