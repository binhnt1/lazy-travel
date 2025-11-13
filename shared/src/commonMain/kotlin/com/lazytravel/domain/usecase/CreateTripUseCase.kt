package com.lazytravel.domain.usecase

import com.lazytravel.domain.model.Trip
import com.lazytravel.domain.repository.TripRepository

/**
 * Use Case - Tạo chuyến đi mới
 */
class CreateTripUseCase(
    private val repository: TripRepository
) {
    suspend operator fun invoke(trip: Trip): Result<Trip> {
        return try {
            // Validation
            if (trip.name.isBlank()) {
                return Result.failure(Exception("Tên chuyến đi không được rỗng"))
            }
            if (trip.startDate.isEmpty()) {
                return Result.failure(Exception("Ngày bắt đầu không được rỗng"))
            }
            if (trip.endDate.isEmpty()) {
                return Result.failure(Exception("Ngày kết thúc không được rỗng"))
            }

            val createdTrip = repository.createTrip(trip)
            Result.success(createdTrip)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
