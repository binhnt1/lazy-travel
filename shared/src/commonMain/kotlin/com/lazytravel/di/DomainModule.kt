package com.lazytravel.di

import com.lazytravel.domain.usecase.*
import org.koin.dsl.module

/**
 * Domain Layer Module - Use Cases
 * Koin DI cho tất cả business logic use cases
 */
val domainModule = module {
    // Destination Use Cases
    factory { GetDestinationsUseCase(get()) }

    // Trip Use Cases
    factory { GetTripsUseCase(get()) }
    factory { CreateTripUseCase(get()) }
    factory { GetTripDetailsUseCase(get()) }

    // Tour Use Cases
    factory { GetToursUseCase(get()) }
    factory { GetPopularToursUseCase(get()) }

    // Post Use Cases (Social Feed)
    factory { GetPostsUseCase(get()) }
    factory { CreatePostUseCase(get()) }

    // Blog Use Cases
    factory { GetBlogPostsUseCase(get()) }

    // Buddy Request Use Cases
    factory { GetBuddyRequestsUseCase(get()) }

    // Notification Use Cases
    factory { GetNotificationsUseCase(get()) }

    // User Profile Use Cases
    factory { GetUserProfileUseCase(get()) }
}
