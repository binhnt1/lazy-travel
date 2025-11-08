package com.lazytravel.domain.model

import kotlinx.serialization.Serializable

/**
 * Domain Model - Địa điểm du lịch
 * Compatible with PocketBase
 */
@Serializable
data class Destination(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val rating: Double = 0.0,
    val price: Double = 0.0,
    val location: String = "",
    val category: String = "",
    val created: String = "",
    val updated: String = ""
)
