package com.lazytravel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lazytravel.ui.components.organisms.HeroSection
import com.lazytravel.ui.theme.AppColors

/**
 * Home Screen - No Authentication
 * Landing page shown to users who are not logged in
 *
 * Based on home_noauth.html
 *
 * Sections:
 * 1. ✅ HeroSection - Main hero with CTA
 * 2. ⏳ StatsBar - Usage statistics (Next)
 * 3. ⏳ FeaturesSection - Key features
 * 4. ⏳ HowItWorksSection - 3-step process
 * 5. ⏳ PopularDestinationsSection - Trending destinations
 * 6. ⏳ TestimonialsSection - User testimonials
 * 7. ⏳ CTASection - Final call to action
 * 8. ⏳ Footer - Links and info
 */
@Composable
fun HomeNoAuthScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
            .verticalScroll(rememberScrollState())
    ) {
        // 1. Hero Section - Complete with gradient, logo, navigation
        HeroSection(
            onLoginClick = {
                // TODO: Navigate to login screen
                println("Login clicked on iOS!")
            },
            onSignupClick = {
                // TODO: Navigate to signup screen
                println("Signup clicked on iOS!")
            }
        )

        // Placeholder for remaining sections
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "✅ HeroSection - Completed",
                color = Color(0xFF4CAF50)
            )
            Text(
                text = "⏳ StatsBar - Building next...",
                color = Color(0xFFFF9800)
            )
            Text(
                text = "⏳ Features Section - Planned",
                color = Color(0xFF666666)
            )
            Text(
                text = "⏳ How It Works - Planned",
                color = Color(0xFF666666)
            )
            Text(
                text = "⏳ Popular Destinations - Planned",
                color = Color(0xFF666666)
            )
            Text(
                text = "⏳ Testimonials - Planned",
                color = Color(0xFF666666)
            )
            Text(
                text = "⏳ Final CTA - Planned",
                color = Color(0xFF666666)
            )
            Text(
                text = "⏳ Footer - Planned",
                color = Color(0xFF666666)
            )
        }
    }
}
