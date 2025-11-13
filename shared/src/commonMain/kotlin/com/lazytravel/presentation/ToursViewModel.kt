package com.lazytravel.presentation

import com.lazytravel.domain.model.Tour
import com.lazytravel.domain.usecase.GetToursUseCase
import com.lazytravel.domain.usecase.GetPopularToursUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Tours ViewModel - Quản lý danh sách tours
 */
class ToursViewModel(
    private val getToursUseCase: GetToursUseCase,
    private val getPopularToursUseCase: GetPopularToursUseCase
) {
    private val viewModelScope = CoroutineScope(Dispatchers.Main)

    private val _uiState = MutableStateFlow<ToursUiState>(ToursUiState.Loading)
    val uiState: StateFlow<ToursUiState> = _uiState.asStateFlow()

    sealed class ToursUiState {
        object Loading : ToursUiState()
        data class Success(val tours: List<Tour>) : ToursUiState()
        data class Error(val message: String) : ToursUiState()
    }

    /**
     * Load tất cả tours
     */
    fun loadTours() {
        viewModelScope.launch {
            _uiState.value = ToursUiState.Loading

            getToursUseCase().fold(
                onSuccess = { tours ->
                    _uiState.value = ToursUiState.Success(tours)
                },
                onFailure = { error ->
                    _uiState.value = ToursUiState.Error(error.message ?: "Unknown error")
                }
            )
        }
    }

    /**
     * Load tours phổ biến
     */
    fun loadPopularTours() {
        viewModelScope.launch {
            _uiState.value = ToursUiState.Loading

            getPopularToursUseCase().fold(
                onSuccess = { tours ->
                    _uiState.value = ToursUiState.Success(tours)
                },
                onFailure = { error ->
                    _uiState.value = ToursUiState.Error(error.message ?: "Unknown error")
                }
            )
        }
    }
}
