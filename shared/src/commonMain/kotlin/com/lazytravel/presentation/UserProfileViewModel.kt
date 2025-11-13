package com.lazytravel.presentation

import com.lazytravel.domain.model.UserProfile
import com.lazytravel.domain.model.UserStats
import com.lazytravel.domain.usecase.GetUserProfileUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * User Profile ViewModel - Quản lý profile người dùng
 */
class UserProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase
) {
    private val viewModelScope = CoroutineScope(Dispatchers.Main)

    private val _uiState = MutableStateFlow<UserProfileUiState>(UserProfileUiState.Loading)
    val uiState: StateFlow<UserProfileUiState> = _uiState.asStateFlow()

    sealed class UserProfileUiState {
        object Loading : UserProfileUiState()
        data class Success(
            val profile: UserProfile,
            val stats: UserStats? = null
        ) : UserProfileUiState()
        data class Error(val message: String) : UserProfileUiState()
    }

    /**
     * Load user profile
     */
    fun loadUserProfile(userId: String) {
        viewModelScope.launch {
            _uiState.value = UserProfileUiState.Loading

            getUserProfileUseCase(userId).fold(
                onSuccess = { profile ->
                    _uiState.value = UserProfileUiState.Success(profile = profile)
                },
                onFailure = { error ->
                    _uiState.value = UserProfileUiState.Error(error.message ?: "Unknown error")
                }
            )
        }
    }
}
