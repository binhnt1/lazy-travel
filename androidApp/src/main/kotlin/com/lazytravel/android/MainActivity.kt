package com.lazytravel.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import com.lazytravel.data.models.Feature
import com.lazytravel.data.models.UseCase
import com.lazytravel.data.remote.PocketBaseClient
import com.lazytravel.ui.screens.HomeNoAuthScreen
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize PocketBase client
        PocketBaseClient.initialize()
        GlobalScope.launch {
            UseCase().setup(true)
            Feature().setup(true)
        }

        setContent {
            MaterialTheme {
                HomeNoAuthScreen()
            }
        }
    }
}
