package com.lazytravel.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Feature Model - from PocketBase "features" collection
 */
@Serializable
data class Feature(
    val id: String,

    @SerialName("icon")
    val icon: String,  // Emoji icon (🗳️, 💰, 📅, 📸)

    @SerialName("title_en")
    val titleEn: String,

    @SerialName("title_vi")
    val titleVi: String,

    @SerialName("description_en")
    val descriptionEn: String,

    @SerialName("description_vi")
    val descriptionVi: String,

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
