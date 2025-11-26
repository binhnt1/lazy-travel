package com.lazytravel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lazytravel.ui.components.sections.BlogSection
import com.lazytravel.ui.components.sections.buddies.BuddyHotSection
import com.lazytravel.ui.components.sections.PlaceSection
import com.lazytravel.ui.components.sections.FeaturesSection
import com.lazytravel.ui.components.sections.FeedSection
import com.lazytravel.ui.components.sections.HeroSection
import com.lazytravel.ui.components.sections.HowItWorkSection
import com.lazytravel.ui.components.sections.ReviewsSection
import com.lazytravel.ui.components.sections.StatsBarSection
import com.lazytravel.ui.components.sections.StatsSection
import com.lazytravel.ui.components.sections.tours.TourHotSection
import com.lazytravel.ui.components.sections.UseCasesSection
import com.lazytravel.ui.theme.AppColors

@Composable
fun HomeNoAuthScreen(
    onNavigateToSignIn: () -> Unit,
    onNavigateToSignUp: () -> Unit = {},
    onNavigateToBuddies: () -> Unit = {},
    onNavigateToPlaces: () -> Unit = {},
    onNavigateToTours: () -> Unit = {},
    onNavigateToBlogs: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
            .verticalScroll(rememberScrollState())
    ) {
        HeroSection(
            onLoginClick = onNavigateToSignIn,
            onSignupClick = onNavigateToSignUp
        )

        StatsBarSection()

        FeaturesSection()

        UseCasesSection()

        StatsSection()

        ReviewsSection()

        BuddyHotSection(
            onJoinClick = {
                onNavigateToSignIn()
            },
            onViewAllClick = onNavigateToBuddies
        )

        PlaceSection(

            onPlaceClick = {
                onNavigateToSignIn()
            },
            onViewAllClick = onNavigateToPlaces
        )

        TourHotSection(
            onTourClick = { tourId ->
                onNavigateToSignIn()
            },
            onViewAllClick = onNavigateToTours
        )

        BlogSection(
            onBlogClick = { blog ->
                onNavigateToSignIn()
            },
            onViewAllClick = onNavigateToBlogs
        )

        FeedSection(
            onLoginClick = onNavigateToSignIn,
            isLoggedIn = false
        )

        HowItWorkSection()
    }
}
