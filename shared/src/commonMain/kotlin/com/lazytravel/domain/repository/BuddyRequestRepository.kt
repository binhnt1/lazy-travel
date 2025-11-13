package com.lazytravel.domain.repository

import com.lazytravel.domain.model.BuddyRequest

/**
 * Buddy Request Repository Interface
 */
interface BuddyRequestRepository {
    suspend fun getBuddyRequests(): List<BuddyRequest>
    suspend fun getOpenRequests(): List<BuddyRequest>
    suspend fun getRequestById(id: String): BuddyRequest
    suspend fun createRequest(request: BuddyRequest): BuddyRequest
    suspend fun updateRequest(request: BuddyRequest): BuddyRequest
    suspend fun deleteRequest(id: String)
}
