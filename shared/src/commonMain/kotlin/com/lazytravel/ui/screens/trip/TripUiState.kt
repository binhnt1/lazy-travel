package com.lazytravel.ui.screens.trip

import com.lazytravel.data.models.Trip
import com.lazytravel.data.models.TripDestination

/**
 * UI State for Create Trip flow
 */
sealed class TripUiState {
    object Idle : TripUiState()
    object Loading : TripUiState()
    data class Success(val trip: Trip) : TripUiState()
    data class Error(val message: String) : TripUiState()
}

/**
 * Form validation state
 */
data class TripFormValidation(
    val step: Int,
    val isValid: Boolean,
    val errors: Map<String, String> = emptyMap()
)

/**
 * Destination for form input
 */
data class DestinationInput(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val estimatedBudget: Double = 0.0,
    val placeId: String = "",
    val cityId: String = "",
    val orderIndex: Int = 0
)
