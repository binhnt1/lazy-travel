package com.lazytravel.data.repositories

import com.lazytravel.data.base.BaseRepository
import com.lazytravel.data.models.Buddy
import com.lazytravel.data.models.enums.BuddyStatus
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class BuddyRepository : BaseRepository<Buddy>() {

    suspend fun getMatchedCount(): Result<Int> {
        return getCount<Buddy>("status='${BuddyStatus.FULL.name}'")
    }

    suspend fun getOpenTripsCount(): Result<Int> {
        return getCount<Buddy>("status='${BuddyStatus.AVAILABLE.name}'")
    }

    suspend fun getThisWeekTripsCount(): Result<Int> {
        val now = Clock.System.now()
        val startTimestamp = now.toEpochMilliseconds()
        val endTimestamp = startTimestamp + (7 * 24 * 60 * 60 * 1000L)
        return getCount<Buddy>("startDate>=$startTimestamp && startDate<=$endTimestamp")
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
