package com.lazytravel.di

import com.lazytravel.presentation.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

/**
 * Koin Helper - Giúp lấy ViewModels từ Koin
 *
 * Cách sử dụng trong UI:
 *
 * Android Compose:
 * ```kotlin
 * @Composable
 * fun DestinationsScreen() {
 *     val viewModel = getViewModel<DestinationViewModel>()
 *     val uiState by viewModel.uiState.collectAsState()
 *     // ...
 * }
 * ```
 *
 * iOS SwiftUI:
 * ```swift
 * struct DestinationsView: View {
 *     let viewModel = KoinHelper().getDestinationViewModel()
 *
 *     var body: some View {
 *         // ...
 *     }
 * }
 * ```
 */
object KoinHelper : KoinComponent {

    // Destination
    fun getDestinationViewModel(): DestinationViewModel = get()

    // Trips
    fun getTripsViewModel(): TripsViewModel = get()

    // Tours
    fun getToursViewModel(): ToursViewModel = get()

    // Posts (Social Feed)
    fun getPostsViewModel(): PostsViewModel = get()

    // Blog
    fun getBlogViewModel(): BlogViewModel = get()

    // Buddy Request
    fun getBuddyRequestViewModel(): BuddyRequestViewModel = get()

    // Notification
    fun getNotificationViewModel(): NotificationViewModel = get()

    // User Profile
    fun getUserProfileViewModel(): UserProfileViewModel = get()
}

/**
 * Generic helper for getting any ViewModel
 */
inline fun <reified T : Any> getViewModel(): T {
    return KoinHelper.get()
}
