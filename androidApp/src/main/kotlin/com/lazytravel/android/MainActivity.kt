package com.lazytravel.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import com.lazytravel.data.remote.PocketBaseClient
import com.lazytravel.ui.screens.HomeNoAuthScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize PocketBase client
        PocketBaseClient.initialize()
        println("✅ PocketBase client initialized for Android")

        setContent {
            MaterialTheme {
                // Home_noauth screen with all sections
                HomeNoAuthScreen()
            }
        }
    }
}
