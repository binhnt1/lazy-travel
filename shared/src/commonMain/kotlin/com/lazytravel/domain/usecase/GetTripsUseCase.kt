package com.lazytravel.domain.usecase

import com.lazytravel.domain.model.Trip
import com.lazytravel.domain.repository.TripRepository

/**
 * Use Case - Lấy danh sách chuyến đi của user
 */
class GetTripsUseCase(
    private val repository: TripRepository
) {
    suspend operator fun invoke(userId: String): Result<List<Trip>> {
        return try {
            val trips = repository.getTrips(userId)
            Result.success(trips)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
