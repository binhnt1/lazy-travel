package com.lazytravel.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.lazytravel.ui.components.organisms.HeroSection

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFFAFAFA)
                ) {
                    TestScreen()
                }
            }
        }
    }
}

@Composable
fun TestScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Test HeroSection - Home-noauth screen
        HeroSection(
            onLoginClick = {
                // TODO: Navigate to login screen
                println("Login clicked")
            },
            onSignupClick = {
                // TODO: Navigate to signup screen
                println("Signup clicked")
            }
        )

        // Placeholder for other sections
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "✅ HeroSection đã hoạt động!",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF212121)
            )
            Text(
                text = "Tiếp tục build các section khác...",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF666666)
            )
        }
    }
}
