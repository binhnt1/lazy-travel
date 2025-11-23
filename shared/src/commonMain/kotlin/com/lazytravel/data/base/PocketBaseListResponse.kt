package com.lazytravel.data.base

import kotlinx.serialization.Serializable


@Serializable
data class PocketBaseListResponse<T>(
    val page: Int,
    val perPage: Int,
    val items: List<T>,
    val totalItems: Int,
    val totalPages: Int,
)