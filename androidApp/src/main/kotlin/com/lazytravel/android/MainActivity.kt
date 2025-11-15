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
        GlobalScope.launch {
            val result = SetupFeaturesCollection.setup()
            result.fold(
                onSuccess = { message -> println("$message\n") },
                onFailure = { error -> println("❌ Setup error: ${error.message}\n") }
            )
        }

        setContent {
            MaterialTheme {
                HomeNoAuthScreen()
            }
        }
    }
}
