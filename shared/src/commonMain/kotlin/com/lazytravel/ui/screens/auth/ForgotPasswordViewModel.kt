package com.lazytravel.ui.screens.auth

import com.lazytravel.data.remote.PocketBaseApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class PasswordResetState {
    object Idle : PasswordResetState()
    object Loading : PasswordResetState()
    object EmailSent : PasswordResetState()
    object ResettingPassword : PasswordResetState()
    object PasswordReset : PasswordResetState()
    data class Error(val message: String) : PasswordResetState()
}

class ForgotPasswordViewModel {
    private val _resetState = MutableStateFlow<PasswordResetState>(PasswordResetState.Idle)
    val resetState: StateFlow<PasswordResetState> = _resetState.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _resetToken = MutableStateFlow("")
    val resetToken: StateFlow<String> = _resetToken.asStateFlow()

    private val _newPassword = MutableStateFlow("")
    val newPassword: StateFlow<String> = _newPassword.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    private val _isPasswordVisible = MutableStateFlow(false)
    val isPasswordVisible: StateFlow<Boolean> = _isPasswordVisible.asStateFlow()

    fun setEmail(value: String) {
        _email.value = value
    }

    fun setResetToken(value: String) {
        _resetToken.value = value
    }

    fun setNewPassword(value: String) {
        _newPassword.value = value
    }

    fun setConfirmPassword(value: String) {
        _confirmPassword.value = value
    }

    fun togglePasswordVisibility() {
        _isPasswordVisible.value = !_isPasswordVisible.value
    }

    fun clearForm() {
        _email.value = ""
        _resetToken.value = ""
        _newPassword.value = ""
        _confirmPassword.value = ""
        _isPasswordVisible.value = false
        _resetState.value = PasswordResetState.Idle
    }

    suspend fun requestPasswordReset() {
        if (!validateEmail()) {
            _resetState.value = PasswordResetState.Error("Vui lòng nhập email hợp lệ")
            return
        }

        _resetState.value = PasswordResetState.Loading

        val result = PocketBaseApi.requestPasswordReset(_email.value)
        result.fold(
            onSuccess = {
                _resetState.value = PasswordResetState.EmailSent
            },
            onFailure = { exception ->
                _resetState.value = PasswordResetState.Error(
                    exception.message ?: "Gửi yêu cầu reset mật khẩu thất bại"
                )
            }
        )
    }

    suspend fun resetPassword() {
        if (!validateResetForm()) {
            _resetState.value = PasswordResetState.Error("Vui lòng điền đầy đủ thông tin")
            return
        }

        _resetState.value = PasswordResetState.ResettingPassword

        val result = PocketBaseApi.resetPassword(
            token = _resetToken.value,
            password = _newPassword.value,
            passwordConfirm = _confirmPassword.value
        )
        result.fold(
            onSuccess = {
                _resetState.value = PasswordResetState.PasswordReset
                clearForm()
            },
            onFailure = { exception ->
                _resetState.value = PasswordResetState.Error(
                    exception.message ?: "Đặt lại mật khẩu thất bại"
                )
            }
        )
    }

    private fun validateEmail(): Boolean {
        val email = _email.value
        return email.isNotEmpty() && email.contains("@")
    }

    private fun validateResetForm(): Boolean {
        return _resetToken.value.isNotEmpty() &&
                _newPassword.value.isNotEmpty() &&
                _confirmPassword.value.isNotEmpty() &&
                _newPassword.value == _confirmPassword.value &&
                _newPassword.value.length >= 6
    }
}
