package com.lazytravel.domain.repository

import com.lazytravel.domain.model.Tour
import com.lazytravel.domain.model.TourReview

/**
 * Tour Repository Interface
 */
interface TourRepository {
    suspend fun getTours(): List<Tour>
    suspend fun getTourById(id: String): Tour
    suspend fun getPopularTours(): List<Tour>
    suspend fun getToursByDestination(destinationId: String): List<Tour>
    suspend fun searchTours(query: String): List<Tour>

    // Reviews
    suspend fun getTourReviews(tourId: String): List<TourReview>
    suspend fun addReview(review: TourReview): TourReview
}
