package com.lazytravel.domain.usecase

import com.lazytravel.domain.model.Destination
import com.lazytravel.domain.repository.DestinationRepository

/**
 * Use Case - Business logic để lấy danh sách địa điểm
 */
class GetDestinationsUseCase(
    private val repository: DestinationRepository
) {
    suspend operator fun invoke(): Result<List<Destination>> {
        return try {
            val destinations = repository.getDestinations()
            Result.success(destinations)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
