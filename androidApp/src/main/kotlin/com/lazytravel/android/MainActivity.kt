package com.lazytravel.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import com.lazytravel.data.remote.PocketBaseClient
import com.lazytravel.data.setup.SetupFeaturesCollection
import com.lazytravel.ui.screens.HomeNoAuthScreen
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize PocketBase client
        PocketBaseClient.initialize()
        println("✅ PocketBase client initialized for Android")

        // Run production data setup (one-time execution)
        // TODO: Remove after first successful run
        GlobalScope.launch {
            println("\n🔧 Running one-time setup for production data...")
            val result = SetupFeaturesCollection.setup()
            result.fold(
                onSuccess = { message -> println("$message\n") },
                onFailure = { error -> println("❌ Setup error: ${error.message}\n") }
            )
        }

        setContent {
            MaterialTheme {
                // Home_noauth screen with all sections
                HomeNoAuthScreen()
            }
        }
    }
}
