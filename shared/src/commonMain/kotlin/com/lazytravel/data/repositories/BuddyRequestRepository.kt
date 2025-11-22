package com.lazytravel.data.repositories

import com.lazytravel.data.base.BaseRepository
import com.lazytravel.data.remote.PocketBaseConfig

class BuddyRequestRepository : BaseRepository(PocketBaseConfig.Collections.BUDDY_REQUESTS) {

    suspend fun getOpenTripsCount(): Result<Int> {
        return getCount("status = 'OPEN'")
    }

    suspend fun getThisWeekTripsCount(): Result<Int> {
        return getCount("status = 'OPEN' && active = true")
    }

    suspend fun getMatchedCount(): Result<Int> {
        return getCount("status = 'MATCHED'")
    }

    suspend fun getTotalTripsCount(): Result<Int> {
        return getCount()
    }

    suspend fun getBuddyStats(): Result<BuddyStats> {
        return try {
            val openTripsResult = getOpenTripsCount()
            val matchedCountResult = getMatchedCount()
            val thisWeekTripsResult = getThisWeekTripsCount()

            when {
                openTripsResult.isFailure -> openTripsResult.map { BuddyStats(0, 0, 0) }
                thisWeekTripsResult.isFailure -> thisWeekTripsResult.map { BuddyStats(0, 0, 0) }
                matchedCountResult.isFailure -> matchedCountResult.map { BuddyStats(0, 0, 0) }
                else -> {
                    val openTrips = openTripsResult.getOrDefault(0)
                    val thisWeekTrips = thisWeekTripsResult.getOrDefault(0)
                    val matchedCount = matchedCountResult.getOrDefault(0)

                    Result.success(BuddyStats(openTrips, thisWeekTrips, matchedCount))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

data class BuddyStats(
    val openTrips: Int,
    val thisWeekTrips: Int,
    val matchedCount: Int
)

// Factory function
fun buddyRequestsRepositoryWithStats() = BuddyRequestRepository()
