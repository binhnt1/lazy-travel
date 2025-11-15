package com.lazytravel.data.remote

import com.lazytravel.data.remote.schema.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Auto Collection Setup Helper
 * Automatically creates collections with schemas if they don't exist
 *
 * Now uses Schema Migration System for better type safety and validation.
 */
object PocketBaseSetup {

    /**
     * List of all collections that don't have schema definitions yet
     * These will be created with empty schemas (schema-less mode)
     *
     * When you create a schema definition for a collection, remove it from this list
     * and add it to the schemas list in ensureCollectionsExist()
     */
    private val legacyCollections = listOf(
        PocketBaseConfig.Collections.HOTELS,
        PocketBaseConfig.Collections.REVIEWS
    )

    /**
     * Ensure all required collections exist
     * Creates them with proper schemas if they don't exist
     *
     * This runs automatically when app starts.
     */
    suspend fun ensureCollectionsExist() = withContext(Dispatchers.Default) {
        try {
            println("🔍 Checking PocketBase collections...")
            println("📋 Using Schema Migration System...")

            // Run schema migrations for collections with defined schemas
            val migrationResult = SchemaMigration.migrate(
                // App Content
                featuresSchema,

                // Destinations
                destinationsSchema,

                // Users & Profile
                userProfilesSchema,
                userStatsSchema,
                achievementsSchema,
                userAchievementsSchema,

                // Trips
                tripsSchema,
                tripMembersSchema,
                tripExpensesSchema,
                tripChecklistsSchema,
                tripChecklistItemsSchema,
                tripNotificationsSchema,

                // Tours
                toursSchema,
                tourReviewsSchema,

                // Travel Buddies
                buddyRequestsSchema,
                buddyRequestTagsSchema,

                // Blog
                blogCategoriesSchema,
                blogPostsSchema,

                // Social Feed
                postsSchema,
                postMediaSchema,
                postLikesSchema,
                postCommentsSchema,

                // Notifications & Photos
                notificationsSchema,
                tripPhotosSchema
            )

            if (!migrationResult) {
                println("⚠️ Some schema migrations failed. Check logs above.")
            }

            // Create legacy collections without schemas (backward compatibility)
            if (legacyCollections.isNotEmpty()) {
                println("\n📦 Creating legacy collections (schema-less)...")

                // Login as admin if not already authenticated
                val authResult = PocketBaseApi.adminAuth(
                    PocketBaseConfig.Admin.EMAIL,
                    PocketBaseConfig.Admin.PASSWORD
                )

                if (authResult.isFailure) {
                    println("⚠️ Admin auth failed: ${authResult.exceptionOrNull()?.message}")
                    println("⚠️ Skipping legacy collection setup.")
                } else {
                    legacyCollections.forEach { collectionName ->
                        try {
                            val exists = PocketBaseApi.collectionExists(collectionName)

                            if (exists) {
                                println("✅ Collection '$collectionName' already exists")
                            } else {
                                // Create collection with empty schema
                                val createResult = PocketBaseApi.createCollection(collectionName)
                                if (createResult.isSuccess) {
                                    println("✅ Created collection '$collectionName' (schema-less)")
                                } else {
                                    println("❌ Failed to create collection '$collectionName': ${createResult.exceptionOrNull()?.message}")
                                }
                            }
                        } catch (e: Exception) {
                            println("❌ Error checking collection '$collectionName': ${e.message}")
                        }
                    }
                }
            }

            println("\n✅ Collection setup complete!")
            println("💡 To add schema for legacy collections, create schema definitions")
            println("   in data/remote/schema/ and add them to ensureCollectionsExist()")

        } catch (e: Exception) {
            println("❌ Collection setup failed: ${e.message}")
            println("💡 You may need to create collections manually in PocketBase Admin UI")
        }
    }

    /**
     * Create a single collection programmatically (legacy method)
     * Consider creating a schema definition instead for better type safety
     */
    @Deprecated(
        message = "Use schema definitions instead for better validation and type safety",
        replaceWith = ReplaceWith("SchemaMigration.migrate(yourSchema)")
    )
    suspend fun createCollection(name: String): Boolean {
        return try {
            val exists = PocketBaseApi.collectionExists(name)
            if (exists) {
                println("ℹ️ Collection '$name' already exists")
                return true
            }

            val result = PocketBaseApi.createCollection(name)
            if (result.isSuccess) {
                println("✅ Created collection '$name'")
                true
            } else {
                println("❌ Failed to create collection '$name'")
                false
            }
        } catch (e: Exception) {
            println("❌ Error creating collection '$name': ${e.message}")
            false
        }
    }
}