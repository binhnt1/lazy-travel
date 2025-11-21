package com.lazytravel.ui.screens.auth

import com.lazytravel.core.i18n.LocalizationManager

/**
 * Validation Helper - Validation utilities for auth forms
 */
object ValidationHelper {
    /**
     * Validate email format
     */
    fun validateEmail(email: String): String? {
        if (email.isBlank()) {
            return LocalizationManager.getString("validation_required")
        }

        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        if (!email.matches(emailRegex)) {
            return LocalizationManager.getString("validation_email_invalid")
        }

        return null
    }

    /**
     * Validate phone number (Vietnamese format)
     */
    fun validatePhone(phone: String): String? {
        if (phone.isBlank()) {
            return LocalizationManager.getString("validation_required")
        }

        // Vietnamese phone number: starts with 0, followed by 9 digits
        val phoneRegex = "^0\\d{9}$".toRegex()
        if (!phone.matches(phoneRegex)) {
            return LocalizationManager.getString("validation_phone_invalid")
        }

        return null
    }

    /**
     * Validate email or phone
     */
    fun validateEmailOrPhone(input: String): String? {
        if (input.isBlank()) {
            return LocalizationManager.getString("validation_required")
        }

        // Check if it's a phone number (starts with 0 and contains only digits)
        if (input.startsWith("0") && input.all { it.isDigit() }) {
            return validatePhone(input)
        }

        // Otherwise, validate as email
        return validateEmail(input)
    }

    /**
     * Validate password
     */
    fun validatePassword(password: String): String? {
        if (password.isBlank()) {
            return LocalizationManager.getString("validation_required")
        }

        if (password.length < 6) {
            return LocalizationManager.getString("validation_password_min_length")
        }

        return null
    }

    /**
     * Validate confirm password matches
     */
    fun validateConfirmPassword(password: String, confirmPassword: String): String? {
        if (confirmPassword.isBlank()) {
            return LocalizationManager.getString("validation_required")
        }

        if (password != confirmPassword) {
            return LocalizationManager.getString("validation_password_mismatch")
        }

        return null
    }

    /**
     * Validate name
     */
    fun validateName(name: String): String? {
        if (name.isBlank()) {
            return LocalizationManager.getString("validation_required")
        }

        if (name.trim().length < 2) {
            return LocalizationManager.getString("validation_name_min_length")
        }

        return null
    }

    /**
     * Validate required field
     */
    fun validateRequired(value: String): String? {
        if (value.isBlank()) {
            return LocalizationManager.getString("validation_required")
        }
        return null
    }
}
