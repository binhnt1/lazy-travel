package com.lazytravel.data.repository

import com.lazytravel.data.remote.PocketBaseApi
import com.lazytravel.data.remote.PocketBaseConfig
import com.lazytravel.domain.model.Tour
import com.lazytravel.domain.model.TourReview
import com.lazytravel.domain.repository.TourRepository
import kotlinx.serialization.json.Json

/**
 * Tour Repository Implementation
 */
class TourRepositoryImpl : TourRepository {

    private val toursCollection = PocketBaseConfig.Collections.TOURS
    private val reviewsCollection = PocketBaseConfig.Collections.TOUR_REVIEWS

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override suspend fun getTours(): List<Tour> {
        val filter = "is_active = true"

        val result = PocketBaseApi.getRecords(
            collection = toursCollection,
            page = 1,
            perPage = 100,
            sort = "-created",
            filter = filter
        )

        return result.fold(
            onSuccess = { response ->
                response.items.mapNotNull { jsonElement ->
                    try {
                        json.decodeFromJsonElement(Tour.serializer(), jsonElement)
                    } catch (e: Exception) {
                        println("⚠️ Failed to parse tour: ${e.message}")
                        null
                    }
                }
            },
            onFailure = { error ->
                println("❌ Error fetching tours: ${error.message}")
                emptyList()
            }
        )
    }

    override suspend fun getTourById(id: String): Tour {
        val result = PocketBaseApi.getRecord(toursCollection, id)

        return result.fold(
            onSuccess = { responseText ->
                json.decodeFromString(Tour.serializer(), responseText)
            },
            onFailure = { error ->
                throw Exception("Error fetching tour $id: ${error.message}")
            }
        )
    }

    override suspend fun getPopularTours(): List<Tour> {
        // Get tours with high rating and active status
        val filter = "is_active = true && rating >= 4.0"

        val result = PocketBaseApi.getRecords(
            collection = toursCollection,
            page = 1,
            perPage = 20,
            sort = "-rating,-reviews_count",
            filter = filter
        )

        return result.fold(
            onSuccess = { response ->
                response.items.mapNotNull { jsonElement ->
                    try {
                        json.decodeFromJsonElement(Tour.serializer(), jsonElement)
                    } catch (e: Exception) {
                        println("⚠️ Failed to parse tour: ${e.message}")
                        null
                    }
                }
            },
            onFailure = { error ->
                println("❌ Error fetching popular tours: ${error.message}")
                emptyList()
            }
        )
    }

    override suspend fun getToursByDestination(destinationId: String): List<Tour> {
        val filter = "destination_id = '$destinationId' && is_active = true"

        val result = PocketBaseApi.getRecords(
            collection = toursCollection,
            filter = filter,
            sort = "-rating"
        )

        return result.fold(
            onSuccess = { response ->
                response.items.mapNotNull { jsonElement ->
                    try {
                        json.decodeFromJsonElement(Tour.serializer(), jsonElement)
                    } catch (e: Exception) {
                        println("⚠️ Failed to parse tour: ${e.message}")
                        null
                    }
                }
            },
            onFailure = { error ->
                println("❌ Error fetching tours by destination: ${error.message}")
                emptyList()
            }
        )
    }

    override suspend fun searchTours(query: String): List<Tour> {
        val filter = "is_active = true && (name ~ '$query' || description ~ '$query')"

        val result = PocketBaseApi.getRecords(
            collection = toursCollection,
            filter = filter,
            sort = "-rating"
        )

        return result.fold(
            onSuccess = { response ->
                response.items.mapNotNull { jsonElement ->
                    try {
                        json.decodeFromJsonElement(Tour.serializer(), jsonElement)
                    } catch (e: Exception) {
                        println("⚠️ Failed to parse tour: ${e.message}")
                        null
                    }
                }
            },
            onFailure = { error ->
                println("❌ Error searching tours: ${error.message}")
                emptyList()
            }
        )
    }

    // Reviews
    override suspend fun getTourReviews(tourId: String): List<TourReview> {
        val filter = "tour_id = '$tourId'"

        val result = PocketBaseApi.getRecords(
            collection = reviewsCollection,
            filter = filter,
            sort = "-created"
        )

        return result.fold(
            onSuccess = { response ->
                response.items.mapNotNull { jsonElement ->
                    try {
                        json.decodeFromJsonElement(TourReview.serializer(), jsonElement)
                    } catch (e: Exception) {
                        println("⚠️ Failed to parse tour review: ${e.message}")
                        null
                    }
                }
            },
            onFailure = { error ->
                println("❌ Error fetching tour reviews: ${error.message}")
                emptyList()
            }
        )
    }

    override suspend fun addReview(review: TourReview): TourReview {
        val result = PocketBaseApi.createRecord(reviewsCollection, review)

        return result.fold(
            onSuccess = { responseText ->
                json.decodeFromString(TourReview.serializer(), responseText)
            },
            onFailure = { error ->
                throw Exception("Error adding review: ${error.message}")
            }
        )
    }
}
