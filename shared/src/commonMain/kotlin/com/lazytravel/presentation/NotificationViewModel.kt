package com.lazytravel.presentation

import com.lazytravel.domain.model.Notification
import com.lazytravel.domain.usecase.GetNotificationsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Notification ViewModel - Quản lý thông báo
 */
class NotificationViewModel(
    private val getNotificationsUseCase: GetNotificationsUseCase
) {
    private val viewModelScope = CoroutineScope(Dispatchers.Main)

    private val _uiState = MutableStateFlow<NotificationUiState>(NotificationUiState.Loading)
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()

    sealed class NotificationUiState {
        object Loading : NotificationUiState()
        data class Success(
            val notifications: List<Notification>,
            val unreadCount: Int
        ) : NotificationUiState()
        data class Error(val message: String) : NotificationUiState()
    }

    /**
     * Load notifications
     */
    fun loadNotifications(userId: String) {
        viewModelScope.launch {
            _uiState.value = NotificationUiState.Loading

            getNotificationsUseCase(userId).fold(
                onSuccess = { notifications ->
                    val unreadCount = notifications.count { !it.isRead }
                    _uiState.value = NotificationUiState.Success(notifications, unreadCount)
                },
                onFailure = { error ->
                    _uiState.value = NotificationUiState.Error(error.message ?: "Unknown error")
                }
            )
        }
    }
}
