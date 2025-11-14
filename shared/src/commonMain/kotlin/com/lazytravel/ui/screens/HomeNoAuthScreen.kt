package com.lazytravel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Temporarily disabled for iOS testing
// import com.lazytravel.ui.components.organisms.HeroSection
// import com.lazytravel.ui.theme.AppColors

/**
 * Home Screen - No Authentication
 * Landing page shown to users who are not logged in
 *
 * Based on home_noauth.html
 *
 * TEMPORARY: Using minimal UI to test Compose iOS compatibility
 */
@Composable
fun HomeNoAuthScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        // Minimal test UI
        Text(
            text = "✈️",
            fontSize = 64.sp
        )

        Text(
            text = "LazyTravel",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF212121)
        )

        Text(
            text = "Compose Multiplatform iOS Test",
            fontSize = 16.sp,
            color = Color(0xFF666666)
        )

        Button(
            onClick = {
                println("Button clicked on iOS!")
            }
        ) {
            Text("Test Button")
        }

        Column(
            modifier = Modifier.padding(top = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("✅ Compose runtime working", color = Color(0xFF4CAF50))
            Text("✅ Material3 components working", color = Color(0xFF4CAF50))
            Text("✅ Button interactions working", color = Color(0xFF4CAF50))
            Text("⏳ Testing HeroSection next...", color = Color(0xFFFF9800))
        }
    }
}
