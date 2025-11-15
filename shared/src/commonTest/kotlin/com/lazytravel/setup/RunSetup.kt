package com.lazytravel.setup

import com.lazytravel.data.setup.SetupFeaturesCollection
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

/**
 * Setup Runner - Execute once to populate PocketBase with production data
 *
 * Run this test to:
 * 1. Create 'features' collection on PocketBase
 * 2. Seed production data (4 features with bilingual content)
 *
 * After successful run, delete this file or comment out the test.
 */
class RunSetup {

    @Test
    fun setupFeaturesCollection() = runBlocking {
        println("========================================")
        println("🚀 Starting Production Data Setup")
        println("========================================")

        val result = SetupFeaturesCollection.setup()

        result.fold(
            onSuccess = { message ->
                println("\n========================================")
                println(message)
                println("========================================")
            },
            onFailure = { error ->
                println("\n========================================")
                println("❌ Setup failed: ${error.message}")
                println("========================================")
                error.printStackTrace()
            }
        )
    }
}
