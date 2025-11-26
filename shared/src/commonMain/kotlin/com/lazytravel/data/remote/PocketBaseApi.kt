package com.lazytravel.data.remote

import com.lazytravel.data.models.AuthResponse
import com.lazytravel.data.models.AuthRecord
import com.lazytravel.data.models.EmailSignUpRequest
import com.lazytravel.data.models.EmailSignInRequest
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

/**
 * PocketBase API Service
 * Helper functions for common PocketBase operations
 */
object PocketBaseApi {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    /**
     * Admin auth - for superuser operations
     * Sets adminToken for collection management (create/update/delete collections)
     */
    suspend fun adminAuth(email: String, password: String): Result<AdminAuthResponse> {
        return try {
            val client = PocketBaseClient.getClient()
            val response: HttpResponse = client.post("/api/collections/_superusers/auth-with-password") {
                contentType(ContentType.Application.Json)
                setBody(mapOf(
                    "identity" to email,
                    "password" to password
                ))
            }
            val responseBody = response.bodyAsText()
            if (response.status.isSuccess()) {
                val authResponse = json.decodeFromString<AdminAuthResponse>(responseBody)
                PocketBaseClient.setAdminToken(authResponse.token)
                Result.success(authResponse)
            } else {
                Result.failure(Exception("Admin auth failed: ${response.status} - $responseBody"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    /**
     * Create collection (admin only)
     * Requires adminToken
     */
    suspend fun createCollection(name: String): Result<Boolean> {
        return withContext(Dispatchers.Default) {
            try {
                val client = PocketBaseClient.getClient()
                val response: HttpResponse = client.post("/api/collections") {
                    contentType(ContentType.Application.Json)
                    PocketBaseClient.adminToken?.let {
                        header("Authorization", it)
                    }
                    setBody(mapOf(
                        "name" to name,
                        "type" to "base",
                        "schema" to emptyList<Any>()
                    ))
                }

                Result.success(response.status.isSuccess())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Delete collection by name
     */
    suspend fun deleteCollection(name: String) {
        val client = PocketBaseClient.getClient()
        try {
            client.delete("/api/collections/$name") {
                PocketBaseClient.adminToken?.let { header("Authorization", it) }
            }
        } catch (_: Exception) {
        }
    }

    /**
     * Check if collection exists (admin only)
     * Requires adminToken
     */
    suspend fun collectionExists(name: String): Boolean {
        return try {
            val client = PocketBaseClient.getClient()
            val response: HttpResponse = client.get("/api/collections/$name") {
                PocketBaseClient.adminToken?.let {
                    header("Authorization", it)
                }
            }

            val exists = response.status.isSuccess()
            exists
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Get records from collection
     */
    suspend fun getRecords(
        collection: String,
        page: Int = 1,
        perPage: Int = 50,
        sort: String? = null,
        filter: String? = null,
        expand: String? = null
    ): Result<RecordsListResponse> {
        return try {
            val client = PocketBaseClient.getClient()
            val response: HttpResponse = client.get("/api/collections/$collection/records") {
                parameter("page", page)
                parameter("perPage", perPage)
                sort?.let { parameter("sort", it) }
                filter?.let { parameter("filter", it) }
                expand?.let { parameter("expand", it) }
                PocketBaseClient.adminToken?.let {
                    header("Authorization", it)
                }
            }

            val responseBody = response.bodyAsText()
            if (response.status.isSuccess()) {
                val recordsResponse = json.decodeFromString<RecordsListResponse>(responseBody)
                Result.success(recordsResponse)
            } else {
                Result.failure(Exception("Failed to get records: ${response.status}"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    /**
     * Get single record
     */
    suspend fun getRecord(collection: String, id: String): Result<String> {
        return try {
            val client = PocketBaseClient.getClient()
            val response: HttpResponse = client.get("/api/collections/$collection/records/$id") {
                PocketBaseClient.adminToken?.let {
                    header("Authorization", it)
                }
            }

            if (response.status.isSuccess()) {
                Result.success(response.bodyAsText())
            } else {
                Result.failure(Exception("Failed to get record: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Create record
     */
    suspend fun createRecord(collection: String, data: Any): Result<String> {
        return try {
            val client = PocketBaseClient.getClient()
            val response: HttpResponse = client.post("/api/collections/$collection/records") {
                contentType(ContentType.Application.Json)
                PocketBaseClient.adminToken?.let {
                    header("Authorization", it)
                }
                setBody(data)
            }

            if (response.status.isSuccess()) {
                Result.success(response.bodyAsText())
            } else {
                val errorBody = response.bodyAsText()
                println("❌ Failed to create record in $collection: ${response.status}")
                println("❌ Error response: $errorBody")
                Result.failure(Exception("Failed to create record: ${response.status} - $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Update record
     */
    suspend fun updateRecord(collection: String, id: String, data: Any): Result<String> {
        return try {
            val client = PocketBaseClient.getClient()
            val response: HttpResponse = client.patch("/api/collections/$collection/records/$id") {
                contentType(ContentType.Application.Json)
                PocketBaseClient.adminToken?.let {
                    header("Authorization", it)
                }
                setBody(data)
            }

            if (response.status.isSuccess()) {
                Result.success(response.bodyAsText())
            } else {
                Result.failure(Exception("Failed to update record: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Delete record
     */
    suspend fun deleteRecord(collection: String, id: String): Result<Boolean> {
        return try {
            val client = PocketBaseClient.getClient()
            val response: HttpResponse = client.delete("/api/collections/$collection/records/$id") {
                PocketBaseClient.adminToken?.let {
                    header("Authorization", it)
                }
            }
            Result.success(response.status.isSuccess())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * User signup with email
     */
    suspend fun signUpWithEmail(request: EmailSignUpRequest): Result<AuthResponse> {
        return try {
            println("DEBUG API: signUpWithEmail called with email: ${request.email}")
            val client = PocketBaseClient.getClient()

            // Step 1: Create the record
            val requestBody = mutableMapOf(
                "email" to request.email,
                "password" to request.password,
                "passwordConfirm" to request.passwordConfirm,
                "username" to request.username.ifEmpty { request.email.substringBefore("@") },
                "fullName" to request.fullName,
                "phone" to (request.phone.ifEmpty { "" })
            )
            println("DEBUG API: Request body: $requestBody")

            val createResponse: HttpResponse = client.post("/api/collections/accounts/records") {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }

            val createResponseBody = createResponse.bodyAsText()
            println("DEBUG API: Create response status: ${createResponse.status}, body: $createResponseBody")

            if (!createResponse.status.isSuccess()) {
                val errorMsg = "Sign up failed: ${createResponse.status} - $createResponseBody"
                println("DEBUG API: Create error - $errorMsg")
                return Result.failure(Exception(errorMsg))
            }

            // Step 2: Parse the record from response
            val authRecord = json.decodeFromString<AuthRecord>(createResponseBody)
            println("DEBUG API: Record created with ID: ${authRecord.id}")

            // Step 3: Authenticate with the same credentials to get token
            println("DEBUG API: Authenticating to get token...")
            val signInRequest = EmailSignInRequest(
                identity = request.email,
                password = request.password
            )
            val signInResult = signInWithEmail(signInRequest)

            return signInResult.fold(
                onSuccess = { authResponse ->
                    println("DEBUG API: Sign up complete! Token obtained")
                    Result.success(authResponse)
                },
                onFailure = { exception ->
                    // Record was created but auth failed
                    println("DEBUG API: Record created but auth failed: ${exception.message}")
                    Result.failure(Exception("Account created but authentication failed: ${exception.message}"))
                }
            )

        } catch (e: Exception) {
            println("DEBUG API: Exception - ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    /**
     * User signin with email/username
     */
    suspend fun signInWithEmail(request: EmailSignInRequest): Result<AuthResponse> {
        return try {
            println("DEBUG API SignIn: signInWithEmail called with identity: ${request.identity}")
            val client = PocketBaseClient.getClient()
            val requestBody = mapOf(
                "identity" to request.identity,
                "password" to request.password
            )
            println("DEBUG API SignIn: Request body: $requestBody")

            val response: HttpResponse = client.post("/api/collections/accounts/auth-with-password") {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }

            val responseBody = response.bodyAsText()
            println("DEBUG API SignIn: Response status: ${response.status}, body: $responseBody")

            if (response.status.isSuccess()) {
                val authResponse = json.decodeFromString<AuthResponse>(responseBody)
                PocketBaseClient.setCollectionToken(authResponse.token)
                println("DEBUG API SignIn: Sign in successful! Token: ${authResponse.token.take(20)}...")
                Result.success(authResponse)
            } else {
                val errorMsg = "Sign in failed: ${response.status} - $responseBody"
                println("DEBUG API SignIn: Error - $errorMsg")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            println("DEBUG API SignIn: Exception - ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    /**
     * Logout - clear token
     */
    fun logout() {
        PocketBaseClient.clearCollectionToken()
    }

    /**
     * Request password reset - sends email with reset link
     */
    suspend fun requestPasswordReset(email: String): Result<PasswordResetResponse> {
        return try {
            val client = PocketBaseClient.getClient()
            val response: HttpResponse = client.post("/api/collections/accounts/request-password-reset") {
                contentType(ContentType.Application.Json)
                setBody(mapOf(
                    "email" to email
                ))
            }

            val responseBody = response.bodyAsText()
            if (response.status.isSuccess()) {
                Result.success(PasswordResetResponse(success = true))
            } else {
                Result.failure(Exception("Password reset request failed: ${response.status} - $responseBody"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    /**
     * Confirm password reset with token
     */
    suspend fun resetPassword(
        token: String,
        password: String,
        passwordConfirm: String
    ): Result<PasswordResetResponse> {
        return try {
            val client = PocketBaseClient.getClient()
            val response: HttpResponse = client.post("/api/collections/accounts/confirm-password-reset") {
                contentType(ContentType.Application.Json)
                setBody(mapOf(
                    "passwordResetToken" to token,
                    "password" to password,
                    "passwordConfirm" to passwordConfirm
                ))
            }

            val responseBody = response.bodyAsText()
            if (response.status.isSuccess()) {
                Result.success(PasswordResetResponse(success = true))
            } else {
                Result.failure(Exception("Password reset failed: ${response.status} - $responseBody"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    /**
     * Debug function to test connection and authentication
     */
    suspend fun debugConnection(): Boolean {
        println("🔍 Starting PocketBase connection debug...")
        
        return try {
            // Test 1: Basic connectivity
            println("1️⃣ Testing basic connectivity to ${PocketBaseConfig.BASE_URL}...")
            val client = PocketBaseClient.getClient()
            val healthResponse: HttpResponse = client.get("/api/health")
            val healthSuccess = healthResponse.status.isSuccess()
            println("   Health check: ${if (healthSuccess) "✅ PASS" else "❌ FAIL"} (${healthResponse.status})")
            
            // Test 2: Admin authentication
            println("2️⃣ Testing admin authentication...")
            val authResult = adminAuth(
                PocketBaseConfig.Admin.EMAIL,
                PocketBaseConfig.Admin.PASSWORD
            )
            authResult.fold(
                onSuccess = {
                    println("   ✅ Admin auth successful")
                    println("   Token: ${it.token.take(20)}...")
                },
                onFailure = { error ->
                    println("   ❌ Admin auth failed: ${error.message}")
                    return false
                }
            )
            
            // Test 3: List collections
            println("3️⃣ Testing collection list access...")
            val listResponse: HttpResponse = client.get("/api/collections") {
                PocketBaseClient.adminToken?.let { header("Authorization", it) }
            }
            val listSuccess = listResponse.status.isSuccess()
            if (listSuccess) {
                val responseBody = listResponse.bodyAsText()
                println("   ✅ Collection list access successful")
                println("   Response preview: ${responseBody.take(100)}...")
            } else {
                println("   ❌ Collection list access failed: ${listResponse.status}")
                return false
            }
            
            println("🎉 All connection tests passed!")
            true
        } catch (e: Exception) {
            println("💥 Connection debug failed: ${e.message}")
            e.printStackTrace()
            false
        }
    }
}

// Response models
@Serializable
data class AdminAuthResponse(
    val token: String,
    val admin: AdminRecord? = null
)

@Serializable
data class AdminRecord(
    val id: String = "",
    val email: String = ""
)

@Serializable
data class RecordsListResponse(
    val page: Int = 1,
    val perPage: Int = 50,
    val totalItems: Int = 0,
    val totalPages: Int = 0,
    val items: List<JsonElement> = emptyList()
)

@Serializable
data class PasswordResetResponse(
    val success: Boolean = true,
    val message: String = ""
)

@Serializable
data class OAuthProvidersResponse(
    val emailPassword: Boolean = false,
    val authProviders: List<OAuthProvider> = emptyList()
)

@Serializable
data class OAuthProvider(
    val name: String = "",
    val state: String = "",
    val codeVerifier: String = "",
    val codeChallenge: String = "",
    val codeChallengeMethod: String = "",
    val authUrl: String = ""
)
