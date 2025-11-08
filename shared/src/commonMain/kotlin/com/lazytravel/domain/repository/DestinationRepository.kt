package com.lazytravel.domain.repository

import com.lazytravel.domain.model.Destination

/**
 * Repository Interface - Định nghĩa các phương thức truy xuất dữ liệu
 */
interface DestinationRepository {
    suspend fun getDestinations(): List<Destination>
    suspend fun getDestinationById(id: String): Destination?
    suspend fun searchDestinations(query: String): List<Destination>
}
