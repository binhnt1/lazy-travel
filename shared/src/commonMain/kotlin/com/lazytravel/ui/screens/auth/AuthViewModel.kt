package com.lazytravel.ui.screens.auth

import com.lazytravel.data.models.AuthRecord
import com.lazytravel.data.models.EmailSignInRequest
import com.lazytravel.data.models.EmailSignUpRequest
import com.lazytravel.data.remote.PocketBaseApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: AuthRecord) : AuthState()
    data class Error(val message: String) : AuthState()
}

sealed class AuthEvent {
    object SignInSuccess : AuthEvent()
    object SignUpSuccess : AuthEvent()
    data class NavigateToHome(val user: AuthRecord) : AuthEvent()
    data class ShowError(val message: String) : AuthEvent()
}

class AuthViewModel {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _phone = MutableStateFlow("")
    val phone: StateFlow<String> = _phone.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName.asStateFlow()

    private val _isPasswordVisible = MutableStateFlow(false)
    val isPasswordVisible: StateFlow<Boolean> = _isPasswordVisible.asStateFlow()

    private val _isSignUp = MutableStateFlow(false)
    val isSignUp: StateFlow<Boolean> = _isSignUp.asStateFlow()

    private val _selectedAuthTab = MutableStateFlow(AuthTab.EMAIL)
    val selectedAuthTab: StateFlow<AuthTab> = _selectedAuthTab.asStateFlow()


    fun setEmail(value: String) {
        _email.value = value
    }

    fun setPhone(value: String) {
        _phone.value = value
    }

    fun setPassword(value: String) {
        _password.value = value
    }

    fun setConfirmPassword(value: String) {
        _confirmPassword.value = value
    }

    fun setFullName(value: String) {
        _fullName.value = value
    }

    fun togglePasswordVisibility() {
        _isPasswordVisible.value = !_isPasswordVisible.value
    }

    fun setIsSignUp(value: Boolean) {
        _isSignUp.value = value
    }

    fun setSelectedAuthTab(tab: AuthTab) {
        _selectedAuthTab.value = tab
    }

    fun clearForm() {
        _email.value = ""
        _phone.value = ""
        _password.value = ""
        _confirmPassword.value = ""
        _fullName.value = ""
        _isPasswordVisible.value = false
    }

    suspend fun signUpWithEmail() {
        println("DEBUG SignUp: signUpWithEmail() called")
        println("DEBUG SignUp: Email: '${_email.value}' (trimmed: '${_email.value.trim()}')")
        println("DEBUG SignUp: FullName: '${_fullName.value}', Password length: ${_password.value.length}")

        if (!validateSignUpForm()) {
            val errorMsg = if (_email.value.trim().isEmpty()) {
                "Email không được để trống"
            } else if (_password.value.isEmpty()) {
                "Mật khẩu không được để trống"
            } else if (_confirmPassword.value.isEmpty()) {
                "Xác nhận mật khẩu không được để trống"
            } else if (_password.value != _confirmPassword.value) {
                "Mật khẩu và xác nhận mật khẩu không khớp"
            } else if (_fullName.value.isEmpty()) {
                "Họ và tên không được để trống"
            } else {
                "Email không hợp lệ. Vui lòng kiểm tra định dạng (ví dụ: email@example.com)"
            }
            println("DEBUG SignUp: Validation failed - $errorMsg")
            println("DEBUG SignUp: Email pattern test: '${_email.value.trim()}' matches pattern = ${_email.value.trim().matches("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex())}")
            _authState.value = AuthState.Error(errorMsg)
            return
        }

        println("DEBUG SignUp: Validation passed, setting Loading state")
        _authState.value = AuthState.Loading

        // Trim email to remove whitespace
        val trimmedEmail = _email.value.trim()

        val request = EmailSignUpRequest(
            email = trimmedEmail,
            password = _password.value,
            passwordConfirm = _confirmPassword.value,
            fullName = _fullName.value,
            username = trimmedEmail.substringBefore("@"),
            phone = _phone.value.trim()  // Add phone field (can be empty)
        )

        println("DEBUG SignUp: Calling PocketBaseApi.signUpWithEmail()")
        println("DEBUG SignUp: Trimmed email: '$trimmedEmail', Phone: '${_phone.value.trim()}'")
        val result = PocketBaseApi.signUpWithEmail(request)
        println("DEBUG SignUp: Got result from API")

        result.fold(
            onSuccess = { authResponse ->
                println("DEBUG SignUp: Sign up successful! User ID: ${authResponse.record.id}")
                _authState.value = AuthState.Success(authResponse.record)
                clearForm()
            },
            onFailure = { exception ->
                println("DEBUG SignUp: Sign up failed: ${exception.message}")
                exception.printStackTrace()
                _authState.value = AuthState.Error(
                    exception.message ?: "Đăng ký thất bại. Vui lòng thử lại."
                )
            }
        )
    }

    suspend fun signInWithEmail() {
        println("DEBUG SignIn: signInWithEmail() called")
        println("DEBUG SignIn: Email: '${_email.value}' (trimmed: '${_email.value.trim()}')")
        println("DEBUG SignIn: Password length: ${_password.value.length}")

        if (!validateSignInForm()) {
            val errorMsg = if (_email.value.trim().isEmpty()) {
                "Email không được để trống"
            } else if (_password.value.isEmpty()) {
                "Mật khẩu không được để trống"
            } else {
                "Email không hợp lệ. Vui lòng kiểm tra định dạng (ví dụ: email@example.com)"
            }
            println("DEBUG SignIn: Validation failed - $errorMsg")
            println("DEBUG SignIn: Email pattern test: '${_email.value.trim()}' matches pattern = ${_email.value.trim().matches("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex())}")
            _authState.value = AuthState.Error(errorMsg)
            return
        }

        println("DEBUG SignIn: Validation passed, setting Loading state")
        _authState.value = AuthState.Loading

        val trimmedEmail = _email.value.trim()
        val trimmedPassword = _password.value.trim()
        val request = EmailSignInRequest(
            identity = trimmedEmail,
            password = trimmedPassword
        )

        println("DEBUG SignIn: Calling PocketBaseApi.signInWithEmail()")
        println("DEBUG SignIn: Request email: '$trimmedEmail', password length: ${trimmedPassword.length}")
        val result = PocketBaseApi.signInWithEmail(request)
        println("DEBUG SignIn: Got result from API")

        result.fold(
            onSuccess = { authResponse ->
                println("DEBUG SignIn: Sign in successful! User ID: ${authResponse.record.id}")
                _authState.value = AuthState.Success(authResponse.record)
                clearForm()
            },
            onFailure = { exception ->
                println("DEBUG SignIn: Sign in failed: ${exception.message}")
                exception.printStackTrace()
                _authState.value = AuthState.Error(
                    exception.message ?: "Đăng nhập thất bại. Vui lòng thử lại."
                )
            }
        )
    }


    fun logout() {
        PocketBaseApi.logout()
        _authState.value = AuthState.Idle
        clearForm()
    }

    private fun validateSignInForm(): Boolean {
        // Check non-empty
        if (_email.value.trim().isEmpty() || _password.value.isEmpty()) {
            return false
        }

        // Validate email format (basic validation that works cross-platform)
        // Accepts: email@domain.com, user.name@domain.co.uk, etc.
        // Rejects: test, test@, @test.com, test@.com
        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex()
        return _email.value.trim().matches(emailPattern)
    }

    private fun validateSignUpForm(): Boolean {
        // Check non-empty
        if (_email.value.trim().isEmpty() || _password.value.isEmpty() ||
            _confirmPassword.value.isEmpty() || _fullName.value.isEmpty()) {
            return false
        }

        // Validate email format (basic validation that works cross-platform)
        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex()
        if (!_email.value.trim().matches(emailPattern)) {
            return false
        }

        // Validate passwords match
        return _password.value == _confirmPassword.value
    }
}

enum class AuthTab {
    EMAIL, PHONE
}
