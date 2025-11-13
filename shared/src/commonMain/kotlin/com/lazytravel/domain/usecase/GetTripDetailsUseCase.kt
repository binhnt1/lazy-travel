package com.lazytravel.domain.usecase

import com.lazytravel.domain.model.Trip
import com.lazytravel.domain.repository.TripRepository

/**
 * Use Case - Lấy chi tiết chuyến đi
 */
class GetTripDetailsUseCase(
    private val repository: TripRepository
) {
    suspend operator fun invoke(tripId: String): Result<Trip> {
        return try {
            val trip = repository.getTripById(tripId)
            Result.success(trip)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
