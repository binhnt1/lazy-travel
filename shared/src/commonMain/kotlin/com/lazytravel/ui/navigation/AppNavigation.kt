package com.lazytravel.ui.navigation

import androidx.compose.runtime.*
import com.lazytravel.core.i18n.LocalizationManager
import com.lazytravel.data.models.Destination
import com.lazytravel.data.models.Tour
import com.lazytravel.data.models.BlogPost
import com.lazytravel.ui.screens.HomeNoAuthScreen
import com.lazytravel.ui.screens.HomeScreen
import com.lazytravel.ui.screens.BuddyScreen
import com.lazytravel.ui.screens.DestinationScreen
import com.lazytravel.ui.screens.DestinationDetailScreen
import com.lazytravel.ui.screens.TourScreen
import com.lazytravel.ui.screens.TourDetailScreen
import com.lazytravel.ui.screens.BlogScreen
import com.lazytravel.ui.screens.BlogDetailScreen
import com.lazytravel.ui.screens.auth.SignInScreen
import com.lazytravel.ui.screens.auth.SignUpScreen
import com.lazytravel.ui.screens.auth.ForgotPasswordScreen
import com.lazytravel.ui.screens.auth.ForgotPasswordViewModel

/**
 * App Navigation - Shared navigation logic for all platforms
 * Uses manual state management for maximum compatibility
 */

sealed class Screen {
    object HomeNoAuth : Screen()
    object SignIn : Screen()
    object SignUp : Screen()
    object ForgotPassword : Screen()
    object Home : Screen()
    object Buddies : Screen()
    object Destinations : Screen()
    data class DestinationDetail(val destination: Destination) : Screen()
    object Tours : Screen()
    data class TourDetail(val tour: Tour) : Screen()
    object Blogs : Screen()
    data class BlogDetail(val blog: BlogPost) : Screen()
}

@Composable
fun AppNavigation() {
    // Observe language changes to force recomposition of all screens
    val currentLanguage by LocalizationManager.currentLanguage.collectAsState()

    var currentScreen by remember { mutableStateOf<Screen>(Screen.HomeNoAuth) }
    val backStack = remember { mutableStateListOf<Screen>() }
    val forgotPasswordViewModel = remember { ForgotPasswordViewModel() }

    fun navigate(screen: Screen) {
        backStack.add(currentScreen)
        currentScreen = screen
    }

    fun pop() {
        if (backStack.isNotEmpty()) {
            currentScreen = backStack.removeLast()
        }
    }

    fun replaceAll(screen: Screen) {
        backStack.clear()
        currentScreen = screen
    }

    // Use language as key to force full recomposition when language changes
    key(currentLanguage) {
        when (currentScreen) {
        is Screen.HomeNoAuth -> {
            HomeNoAuthScreen(
                onNavigateToSignIn = { navigate(Screen.SignIn) },
                onNavigateToSignUp = { navigate(Screen.SignUp) },
                onNavigateToBuddies = { navigate(Screen.Buddies) },
                onNavigateToDestinations = { navigate(Screen.Destinations) },
                onNavigateToTours = { navigate(Screen.Tours) },
                onNavigateToBlogs = { navigate(Screen.Blogs) }
            )
        }

        is Screen.SignIn -> {
            SignInScreen(
                onNavigateBack = { pop() },
                onSignUpClick = { navigate(Screen.SignUp) },
                onForgotPasswordClick = { navigate(Screen.ForgotPassword) },
                onSignInSuccess = { replaceAll(Screen.Home) }
            )
        }

        is Screen.SignUp -> {
            SignUpScreen(
                onNavigateBack = { pop() },
                onSignInClick = { pop() },
                onSignUpSuccess = { replaceAll(Screen.Home) }
            )
        }

        is Screen.ForgotPassword -> {
            ForgotPasswordScreen(
                viewModel = forgotPasswordViewModel,
                onBackClick = { pop() },
                onResetSuccess = { pop() }
            )
        }

        is Screen.Home -> {
            HomeScreen(
                onNavigateToProfile = { /* TODO */ },
                onNavigateToTrips = { /* TODO */ },
                onNavigateToFeed = { /* TODO */ }
            )
        }

        is Screen.Buddies -> {
            BuddyScreen(
                onNavigateBack = { pop() },
                onNavigateToDetail = {  }
            )
        }

        is Screen.Destinations -> {
            DestinationScreen(
                onNavigateBack = { pop() },
                onSelectDestination = { destination -> navigate(Screen.DestinationDetail(destination)) }
            )
        }

        is Screen.DestinationDetail -> {
            DestinationDetailScreen(
                destination = (currentScreen as Screen.DestinationDetail).destination,
                onNavigateBack = { pop() },
                onExploreClick = { navigate(Screen.SignIn) }
            )
        }

        is Screen.Tours -> {
            TourScreen(
                onNavigateBack = { pop() },
                onSelectTour = { tour -> navigate(Screen.TourDetail(tour)) }
            )
        }

        is Screen.TourDetail -> {
            TourDetailScreen(
                tour = (currentScreen as Screen.TourDetail).tour,
                onNavigateBack = { pop() },
                onBookClick = { navigate(Screen.SignIn) }
            )
        }

        is Screen.Blogs -> {
            BlogScreen(
                onNavigateBack = { pop() },
                onSelectBlog = { blog -> navigate(Screen.BlogDetail(blog)) }
            )
        }

        is Screen.BlogDetail -> {
            BlogDetailScreen(
                blog = (currentScreen as Screen.BlogDetail).blog,
                onNavigateBack = { pop() }
            )
        }
        }
    }
}
