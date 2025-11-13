package com.lazytravel.presentation

import com.lazytravel.domain.model.BuddyRequest
import com.lazytravel.domain.usecase.GetBuddyRequestsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Buddy Request ViewModel - Quản lý tìm bạn đồng hành
 */
class BuddyRequestViewModel(
    private val getBuddyRequestsUseCase: GetBuddyRequestsUseCase
) {
    private val viewModelScope = CoroutineScope(Dispatchers.Main)

    private val _uiState = MutableStateFlow<BuddyRequestUiState>(BuddyRequestUiState.Loading)
    val uiState: StateFlow<BuddyRequestUiState> = _uiState.asStateFlow()

    sealed class BuddyRequestUiState {
        object Loading : BuddyRequestUiState()
        data class Success(val requests: List<BuddyRequest>) : BuddyRequestUiState()
        data class Error(val message: String) : BuddyRequestUiState()
    }

    /**
     * Load buddy requests
     */
    fun loadBuddyRequests() {
        viewModelScope.launch {
            _uiState.value = BuddyRequestUiState.Loading

            getBuddyRequestsUseCase().fold(
                onSuccess = { requests ->
                    _uiState.value = BuddyRequestUiState.Success(requests)
                },
                onFailure = { error ->
                    _uiState.value = BuddyRequestUiState.Error(error.message ?: "Unknown error")
                }
            )
        }
    }
}
