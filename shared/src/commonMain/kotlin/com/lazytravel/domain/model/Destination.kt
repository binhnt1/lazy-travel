package com.lazytravel.domain.model

/**
 * Domain Model - Địa điểm du lịch
 */
data class Destination(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val rating: Double,
    val price: Double
)
