package com.lazytravel.di

import com.lazytravel.data.repository.*
import com.lazytravel.domain.repository.*
import org.koin.dsl.module

/**
 * Data Layer Module - Repository Implementations
 * Koin DI cho tất cả repositories
 */
val dataModule = module {
    // Destination Repository
    single<DestinationRepository> { DestinationRepositoryImpl() }

    // Trip Repository
    single<TripRepository> { TripRepositoryImpl() }

    // Tour Repository
    single<TourRepository> { TourRepositoryImpl() }

    // Post Repository (Social Feed)
    single<PostRepository> { PostRepositoryImpl() }

    // Blog Repository
    single<BlogRepository> { BlogRepositoryImpl() }

    // Buddy Request Repository
    single<BuddyRequestRepository> { BuddyRequestRepositoryImpl() }

    // Notification Repository
    single<NotificationRepository> { NotificationRepositoryImpl() }

    // User Profile Repository
    single<UserProfileRepository> { UserProfileRepositoryImpl() }
}
