package com.lazytravel.presentation

import com.lazytravel.domain.model.Trip
import com.lazytravel.domain.model.TripMember
import com.lazytravel.domain.model.TripExpense
import com.lazytravel.domain.model.TripPhoto
import com.lazytravel.domain.usecase.GetTripsUseCase
import com.lazytravel.domain.usecase.CreateTripUseCase
import com.lazytravel.domain.usecase.GetTripDetailsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Trips ViewModel - Quản lý danh sách chuyến đi
 */
class TripsViewModel(
    private val getTripsUseCase: GetTripsUseCase,
    private val createTripUseCase: CreateTripUseCase,
    private val getTripDetailsUseCase: GetTripDetailsUseCase
) {
    private val viewModelScope = CoroutineScope(Dispatchers.Main)

    // UI State cho danh sách trips
    private val _tripsState = MutableStateFlow<TripsUiState>(TripsUiState.Loading)
    val tripsState: StateFlow<TripsUiState> = _tripsState.asStateFlow()

    // UI State cho trip details
    private val _tripDetailsState = MutableStateFlow<TripDetailsUiState>(TripDetailsUiState.Idle)
    val tripDetailsState: StateFlow<TripDetailsUiState> = _tripDetailsState.asStateFlow()

    // UI State cho create trip
    private val _createTripState = MutableStateFlow<CreateTripUiState>(CreateTripUiState.Idle)
    val createTripState: StateFlow<CreateTripUiState> = _createTripState.asStateFlow()

    sealed class TripsUiState {
        object Loading : TripsUiState()
        data class Success(val trips: List<Trip>) : TripsUiState()
        data class Error(val message: String) : TripsUiState()
    }

    sealed class TripDetailsUiState {
        object Idle : TripDetailsUiState()
        object Loading : TripDetailsUiState()
        data class Success(
            val trip: Trip,
            val members: List<TripMember> = emptyList(),
            val expenses: List<TripExpense> = emptyList(),
            val photos: List<TripPhoto> = emptyList()
        ) : TripDetailsUiState()
        data class Error(val message: String) : TripDetailsUiState()
    }

    sealed class CreateTripUiState {
        object Idle : CreateTripUiState()
        object Loading : CreateTripUiState()
        data class Success(val trip: Trip) : CreateTripUiState()
        data class Error(val message: String) : CreateTripUiState()
    }

    /**
     * Load danh sách trips của user
     */
    fun loadTrips(userId: String) {
        viewModelScope.launch {
            _tripsState.value = TripsUiState.Loading

            getTripsUseCase(userId).fold(
                onSuccess = { trips ->
                    _tripsState.value = TripsUiState.Success(trips)
                },
                onFailure = { error ->
                    _tripsState.value = TripsUiState.Error(error.message ?: "Unknown error")
                }
            )
        }
    }

    /**
     * Load chi tiết trip
     */
    fun loadTripDetails(tripId: String) {
        viewModelScope.launch {
            _tripDetailsState.value = TripDetailsUiState.Loading

            getTripDetailsUseCase(tripId).fold(
                onSuccess = { trip ->
                    _tripDetailsState.value = TripDetailsUiState.Success(trip = trip)
                },
                onFailure = { error ->
                    _tripDetailsState.value = TripDetailsUiState.Error(error.message ?: "Unknown error")
                }
            )
        }
    }

    /**
     * Tạo chuyến đi mới
     */
    fun createTrip(trip: Trip) {
        viewModelScope.launch {
            _createTripState.value = CreateTripUiState.Loading

            createTripUseCase(trip).fold(
                onSuccess = { createdTrip ->
                    _createTripState.value = CreateTripUiState.Success(createdTrip)
                },
                onFailure = { error ->
                    _createTripState.value = CreateTripUiState.Error(error.message ?: "Unknown error")
                }
            )
        }
    }

    /**
     * Reset create trip state
     */
    fun resetCreateTripState() {
        _createTripState.value = CreateTripUiState.Idle
    }
}
