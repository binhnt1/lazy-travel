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
import com.lazytravel.ui.components.organisms.FeaturesSection
import com.lazytravel.ui.components.organisms.HeroSection
import com.lazytravel.ui.components.organisms.StatsBar
import com.lazytravel.ui.components.organisms.UseCasesSection
import com.lazytravel.ui.theme.AppColors

/**
 * Home Screen - No Authentication
 * Landing page shown to users who are not logged in
 *
 * Based on home_noauth.html
 *
 * Sections:
 * 1. ✅ HeroSection - Main hero with CTA
 * 2. ✅ StatsBar - Usage statistics
 * 3. ✅ FeaturesSection - Key features (from PocketBase)
 * 4. ✅ UseCasesSection - DÀNH CHO AI section (from PocketBase)
 * 5. ⏳ HowItWorksSection - 3-step process (Next)
 * 6. ⏳ PopularDestinationsSection - Trending destinations
 * 7. ⏳ TestimonialsSection - User testimonials
 * 8. ⏳ CTASection - Final call to action
 * 9. ⏳ Footer - Links and info
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

        // 2. Stats Bar - 4 statistics (users, trips, destinations, rating)
        StatsBar()

        // 3. Features Section - Fetches from PocketBase
        FeaturesSection()

        // 4. Use Cases Section - DÀNH CHO AI
        UseCasesSection()

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
                text = "✅ StatsBar - Completed",
                color = Color(0xFF4CAF50)
            )
            Text(
                text = "✅ FeaturesSection - Completed (PocketBase)",
                color = Color(0xFF4CAF50)
            )
            Text(
                text = "✅ UseCasesSection - Completed (PocketBase)",
                color = Color(0xFF4CAF50)
            )
            Text(
                text = "⏳ How It Works - Building next...",
                color = Color(0xFFFF9800)
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
