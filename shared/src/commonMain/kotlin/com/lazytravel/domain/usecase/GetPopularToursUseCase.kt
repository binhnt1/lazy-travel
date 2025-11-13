package com.lazytravel.domain.usecase

import com.lazytravel.domain.model.Tour
import com.lazytravel.domain.repository.TourRepository

/**
 * Use Case - Lấy danh sách tours phổ biến
 */
class GetPopularToursUseCase(
    private val repository: TourRepository
) {
    suspend operator fun invoke(): Result<List<Tour>> {
        return try {
            val tours = repository.getPopularTours()
            Result.success(tours)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
