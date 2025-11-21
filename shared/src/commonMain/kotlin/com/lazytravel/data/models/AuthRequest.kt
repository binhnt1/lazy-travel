package com.lazytravel.data.models

import kotlinx.serialization.Serializable

/**
 * Authentication request models
 */
@Serializable
data class EmailSignUpRequest(
    val email: String,
    val password: String,
    val passwordConfirm: String,
    val fullName: String = "",
    val username: String = "",
    val phone: String = ""  // Optional phone field
)

@Serializable
data class EmailSignInRequest(
    val identity: String, // email or username
    val password: String
)

@Serializable
data class PhoneSignUpRequest(
    val phone: String,
    val password: String,
    val passwordConfirm: String,
    val fullName: String = ""
)

@Serializable
data class PhoneSignInRequest(
    val phone: String,
    val password: String
)

@Serializable
data class OAuthRequest(
    val provider: String, // "google", "apple"
    val idToken: String,
    val accessToken: String? = null
)

@Serializable
data class AuthResponse(
    val record: AuthRecord,
    val token: String
)

@Serializable
data class AuthRecord(
    val id: String,
    val email: String = "",
    val phone: String = "",
    val username: String = "",
    val fullName: String = "",
    val avatar: String = "",
    val verified: Boolean = false,
    val created: String = "",
    val updated: String = ""
)

@Serializable
data class OAuthTokenResponse(
    val accessToken: String,
    val refreshToken: String? = null,
    val expiresIn: Int? = null
)
