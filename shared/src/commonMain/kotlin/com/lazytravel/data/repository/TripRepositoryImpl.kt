package com.lazytravel.data.repository

import com.lazytravel.data.remote.PocketBaseApi
import com.lazytravel.data.remote.PocketBaseConfig
import com.lazytravel.domain.model.*
import com.lazytravel.domain.repository.TripRepository
import kotlinx.serialization.json.Json

/**
 * Trip Repository Implementation
 */
class TripRepositoryImpl : TripRepository {

    private val tripsCollection = PocketBaseConfig.Collections.TRIPS
    private val membersCollection = PocketBaseConfig.Collections.TRIP_MEMBERS
    private val expensesCollection = PocketBaseConfig.Collections.TRIP_EXPENSES
    private val photosCollection = PocketBaseConfig.Collections.TRIP_PHOTOS

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override suspend fun getTrips(userId: String): List<Trip> {
        // Filter trips where user is creator or member
        val filter = "created_by = '$userId'"

        val result = PocketBaseApi.getRecords(
            collection = tripsCollection,
            page = 1,
            perPage = 100,
            sort = "-created",
            filter = filter
        )

        return result.fold(
            onSuccess = { response ->
                response.items.mapNotNull { jsonElement ->
                    try {
                        json.decodeFromJsonElement(Trip.serializer(), jsonElement)
                    } catch (e: Exception) {
                        println("⚠️ Failed to parse trip: ${e.message}")
                        null
                    }
                }
            },
            onFailure = { error ->
                println("❌ Error fetching trips: ${error.message}")
                emptyList()
            }
        )
    }

    override suspend fun getTripById(id: String): Trip {
        val result = PocketBaseApi.getRecord(tripsCollection, id)

        return result.fold(
            onSuccess = { responseText ->
                json.decodeFromString(Trip.serializer(), responseText)
            },
            onFailure = { error ->
                throw Exception("Error fetching trip $id: ${error.message}")
            }
        )
    }

    override suspend fun createTrip(trip: Trip): Trip {
        val result = PocketBaseApi.createRecord(tripsCollection, trip)

        return result.fold(
            onSuccess = { responseText ->
                json.decodeFromString(Trip.serializer(), responseText)
            },
            onFailure = { error ->
                throw Exception("Error creating trip: ${error.message}")
            }
        )
    }

    override suspend fun updateTrip(trip: Trip): Trip {
        val result = PocketBaseApi.updateRecord(tripsCollection, trip.id, trip)

        return result.fold(
            onSuccess = { responseText ->
                json.decodeFromString(Trip.serializer(), responseText)
            },
            onFailure = { error ->
                throw Exception("Error updating trip: ${error.message}")
            }
        )
    }

    override suspend fun deleteTrip(id: String) {
        val result = PocketBaseApi.deleteRecord(tripsCollection, id)

        result.fold(
            onSuccess = { /* Success */ },
            onFailure = { error ->
                throw Exception("Error deleting trip: ${error.message}")
            }
        )
    }

    // Trip Members
    override suspend fun getTripMembers(tripId: String): List<TripMember> {
        val filter = "trip_id = '$tripId'"

        val result = PocketBaseApi.getRecords(
            collection = membersCollection,
            filter = filter
        )

        return result.fold(
            onSuccess = { response ->
                response.items.mapNotNull { jsonElement ->
                    try {
                        json.decodeFromJsonElement(TripMember.serializer(), jsonElement)
                    } catch (e: Exception) {
                        println("⚠️ Failed to parse trip member: ${e.message}")
                        null
                    }
                }
            },
            onFailure = { error ->
                println("❌ Error fetching trip members: ${error.message}")
                emptyList()
            }
        )
    }

    override suspend fun addTripMember(member: TripMember): TripMember {
        val result = PocketBaseApi.createRecord(membersCollection, member)

        return result.fold(
            onSuccess = { responseText ->
                json.decodeFromString(TripMember.serializer(), responseText)
            },
            onFailure = { error ->
                throw Exception("Error adding trip member: ${error.message}")
            }
        )
    }

    override suspend fun removeTripMember(memberId: String) {
        val result = PocketBaseApi.deleteRecord(membersCollection, memberId)

        result.fold(
            onSuccess = { /* Success */ },
            onFailure = { error ->
                throw Exception("Error removing trip member: ${error.message}")
            }
        )
    }

    // Trip Expenses
    override suspend fun getTripExpenses(tripId: String): List<TripExpense> {
        val filter = "trip_id = '$tripId'"

        val result = PocketBaseApi.getRecords(
            collection = expensesCollection,
            filter = filter,
            sort = "-date"
        )

        return result.fold(
            onSuccess = { response ->
                response.items.mapNotNull { jsonElement ->
                    try {
                        json.decodeFromJsonElement(TripExpense.serializer(), jsonElement)
                    } catch (e: Exception) {
                        println("⚠️ Failed to parse trip expense: ${e.message}")
                        null
                    }
                }
            },
            onFailure = { error ->
                println("❌ Error fetching trip expenses: ${error.message}")
                emptyList()
            }
        )
    }

    override suspend fun addExpense(expense: TripExpense): TripExpense {
        val result = PocketBaseApi.createRecord(expensesCollection, expense)

        return result.fold(
            onSuccess = { responseText ->
                json.decodeFromString(TripExpense.serializer(), responseText)
            },
            onFailure = { error ->
                throw Exception("Error adding expense: ${error.message}")
            }
        )
    }

    override suspend fun deleteExpense(expenseId: String) {
        val result = PocketBaseApi.deleteRecord(expensesCollection, expenseId)

        result.fold(
            onSuccess = { /* Success */ },
            onFailure = { error ->
                throw Exception("Error deleting expense: ${error.message}")
            }
        )
    }

    // Trip Photos
    override suspend fun getTripPhotos(tripId: String): List<TripPhoto> {
        val filter = "trip_id = '$tripId'"

        val result = PocketBaseApi.getRecords(
            collection = photosCollection,
            filter = filter,
            sort = "-created"
        )

        return result.fold(
            onSuccess = { response ->
                response.items.mapNotNull { jsonElement ->
                    try {
                        json.decodeFromJsonElement(TripPhoto.serializer(), jsonElement)
                    } catch (e: Exception) {
                        println("⚠️ Failed to parse trip photo: ${e.message}")
                        null
                    }
                }
            },
            onFailure = { error ->
                println("❌ Error fetching trip photos: ${error.message}")
                emptyList()
            }
        )
    }

    override suspend fun uploadPhoto(photo: TripPhoto): TripPhoto {
        val result = PocketBaseApi.createRecord(photosCollection, photo)

        return result.fold(
            onSuccess = { responseText ->
                json.decodeFromString(TripPhoto.serializer(), responseText)
            },
            onFailure = { error ->
                throw Exception("Error uploading photo: ${error.message}")
            }
        )
    }
}
