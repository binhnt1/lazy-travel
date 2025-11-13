package com.lazytravel.domain.usecase

import com.lazytravel.domain.model.BuddyRequest
import com.lazytravel.domain.repository.BuddyRequestRepository

/**
 * Use Case - Lấy danh sách buddy requests
 */
class GetBuddyRequestsUseCase(
    private val repository: BuddyRequestRepository
) {
    suspend operator fun invoke(): Result<List<BuddyRequest>> {
        return try {
            val requests = repository.getOpenRequests()
            Result.success(requests)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
