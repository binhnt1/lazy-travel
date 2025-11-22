package com.lazytravel.data.repositories

import com.lazytravel.data.base.BaseRepository
import com.lazytravel.data.models.BuddyReview
import com.lazytravel.data.models.UserStatsSummary

class BuddyReviewRepository : BaseRepository<BuddyReview>() {

    suspend fun getUserStats(userId: String): Result<UserStatsSummary> {
        return try {
            // Bước 1: Lấy tất cả Buddy của user này
            val buddyRepo = BaseRepository<com.lazytravel.data.models.Buddy>()
            val buddiesResult = buddyRepo.getRecords<com.lazytravel.data.models.Buddy>(
                filter = "userId = '$userId'",
                perPage = 500
            )

            // Bước 2: Từ danh sách Buddy, lấy tất cả BuddyReview
            val buddyIds = buddiesResult.getOrNull()?.map { it.id } ?: emptyList()

            if (buddyIds.isEmpty()) {
                return Result.success(UserStatsSummary(
                    userId = userId,
                    averageRating = 0.0,
                    totalReviews = 0,
                    totalTrips = 0,
                    verifiedBadge = false
                ))
            }

            // Query reviews với filter IN buddyIds
            val filter = buddyIds.joinToString(" || ") { "buddyId = '$it'" }
            val reviewsResult = getRecords<BuddyReview>(
                filter = "($filter)",
                perPage = 500
            )

            reviewsResult.map { reviews ->
                if (reviews.isEmpty()) {
                    UserStatsSummary(
                        userId = userId,
                        averageRating = 0.0,
                        totalReviews = 0,
                        totalTrips = 0,
                        verifiedBadge = false
                    )
                } else {
                    // Tính toán thống kê
                    val totalReviews = reviews.size
                    val averageRating = reviews.map { it.rating }.average()

                    // Đếm số trips duy nhất (distinct buddyId)
                    val totalTrips = reviews.map { it.buddyId }.distinct().size

                    // Verified nếu có >= 5 reviews và rating >= 4.5
                    val verifiedBadge = totalReviews >= 5 && averageRating >= 4.5

                    UserStatsSummary(
                        userId = userId,
                        averageRating = averageRating,
                        totalReviews = totalReviews,
                        totalTrips = totalTrips,
                        verifiedBadge = verifiedBadge
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun getBatchUserStats(userIds: List<String>): Result<Map<String, UserStatsSummary>> {
        return try {
            val statsMap = mutableMapOf<String, UserStatsSummary>()

            // Bước 1: Query tất cả Buddy của các users
            val buddyRepo = BaseRepository<com.lazytravel.data.models.Buddy>()
            val buddyFilter = userIds.joinToString(" || ") { "userId = '$it'" }
            val buddiesResult = buddyRepo.getRecords<com.lazytravel.data.models.Buddy>(
                filter = "($buddyFilter)",
                perPage = 500
            )

            val buddies = buddiesResult.getOrNull() ?: emptyList()
            if (buddies.isEmpty()) {
                // Trả về empty stats cho tất cả users
                userIds.forEach { userId ->
                    statsMap[userId] = UserStatsSummary(
                        userId = userId,
                        averageRating = 0.0,
                        totalReviews = 0,
                        totalTrips = 0,
                        verifiedBadge = false
                    )
                }
                return Result.success(statsMap)
            }

            // Tạo map buddyId -> userId để group sau này
            val buddyUserMap = buddies.associate { it.id to it.userId }
            val buddyIds = buddies.map { it.id }

            // Bước 2: Query tất cả BuddyReview của các buddies
            val reviewFilter = buddyIds.joinToString(" || ") { "buddyId = '$it'" }
            val reviewsResult = getRecords<BuddyReview>(
                filter = "($reviewFilter)",
                perPage = 500
            )

            val allReviews = reviewsResult.getOrNull() ?: emptyList()

            // Bước 3: Group reviews theo userId
            val reviewsByUser = allReviews.groupBy { review ->
                buddyUserMap[review.buddyId] ?: ""
            }

            // Bước 4: Tính toán stats cho từng user
            userIds.forEach { userId ->
                val userReviews = reviewsByUser[userId] ?: emptyList()

                if (userReviews.isEmpty()) {
                    statsMap[userId] = UserStatsSummary(
                        userId = userId,
                        averageRating = 0.0,
                        totalReviews = 0,
                        totalTrips = 0,
                        verifiedBadge = false
                    )
                } else {
                    val totalReviews = userReviews.size
                    val averageRating = userReviews.map { it.rating }.average()
                    val totalTrips = userReviews.map { it.buddyId }.distinct().size
                    val verifiedBadge = totalReviews >= 5 && averageRating >= 4.5

                    statsMap[userId] = UserStatsSummary(
                        userId = userId,
                        averageRating = averageRating,
                        totalReviews = totalReviews,
                        totalTrips = totalTrips,
                        verifiedBadge = verifiedBadge
                    )
                }
            }

            Result.success(statsMap)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}