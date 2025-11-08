package com.lazytravel.presentation

import com.lazytravel.domain.model.Destination
import com.lazytravel.domain.usecase.GetDestinationsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel - Quản lý UI state và business logic
 * Shared giữa Android và iOS
 */
class DestinationViewModel(
    private val getDestinationsUseCase: GetDestinationsUseCase
) {
    private val viewModelScope = CoroutineScope(Dispatchers.Main)

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    sealed class UiState {
        object Loading : UiState()
        data class Success(val destinations: List<Destination>) : UiState()
        data class Error(val message: String) : UiState()
    }

    fun loadDestinations() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            getDestinationsUseCase().fold(
                onSuccess = { destinations ->
                    _uiState.value = UiState.Success(destinations)
                },
                onFailure = { error ->
                    _uiState.value = UiState.Error(error.message ?: "Unknown error")
                }
            )
        }
    }
}
