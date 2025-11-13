package com.lazytravel.domain.repository

import com.lazytravel.domain.model.*

/**
 * Trip Repository Interface
 */
interface TripRepository {
    suspend fun getTrips(userId: String): List<Trip>
    suspend fun getTripById(id: String): Trip
    suspend fun createTrip(trip: Trip): Trip
    suspend fun updateTrip(trip: Trip): Trip
    suspend fun deleteTrip(id: String)

    // Trip Members
    suspend fun getTripMembers(tripId: String): List<TripMember>
    suspend fun addTripMember(member: TripMember): TripMember
    suspend fun removeTripMember(memberId: String)

    // Trip Expenses
    suspend fun getTripExpenses(tripId: String): List<TripExpense>
    suspend fun addExpense(expense: TripExpense): TripExpense
    suspend fun deleteExpense(expenseId: String)

    // Trip Photos
    suspend fun getTripPhotos(tripId: String): List<TripPhoto>
    suspend fun uploadPhoto(photo: TripPhoto): TripPhoto
}
