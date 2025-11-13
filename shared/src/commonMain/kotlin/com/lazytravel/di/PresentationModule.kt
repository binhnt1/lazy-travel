package com.lazytravel.di

import com.lazytravel.presentation.*
import org.koin.dsl.module

/**
 * Presentation Layer Module - ViewModels
 * Koin DI cho tất cả ViewModels
 */
val presentationModule = module {
    // Destination ViewModel
    factory { DestinationViewModel(get()) }

    // Trips ViewModel
    factory { TripsViewModel(get(), get(), get()) }

    // Tours ViewModel
    factory { ToursViewModel(get(), get()) }

    // Posts ViewModel (Social Feed)
    factory { PostsViewModel(get(), get()) }

    // Blog ViewModel
    factory { BlogViewModel(get()) }

    // Buddy Request ViewModel
    factory { BuddyRequestViewModel(get()) }

    // Notification ViewModel
    factory { NotificationViewModel(get()) }

    // User Profile ViewModel
    factory { UserProfileViewModel(get()) }
}
