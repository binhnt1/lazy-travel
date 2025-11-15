package com.lazytravel.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Feature Model - from PocketBase "features" collection
 * Single language - translations handled in app
 */
@Serializable
data class Feature(
    val id: String,

    @SerialName("icon")
    val icon: String,  // Emoji icon (🗳️, 💰, 📅, 📸)

    @SerialName("title")
    val title: String,  // Translation key (e.g., "feature_voting")

    @SerialName("description")
    val description: String,  // Translation key (e.g., "feature_voting_desc")

    @SerialName("order")
    val order: Int = 0,  // Display order

    @SerialName("active")
    val active: Boolean = true
)

/**
 * PocketBase API Response wrapper
 */
@Serializable
data class PocketBaseListResponse<T>(
    val page: Int,
    val perPage: Int,
    val totalItems: Int,
    val totalPages: Int,
    val items: List<T>
)
