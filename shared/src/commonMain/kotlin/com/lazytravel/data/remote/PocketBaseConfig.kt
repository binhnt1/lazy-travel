package com.lazytravel.data.remote

/**
 * PocketBase Configuration
 * Change baseUrl when you have a domain/SSL
 */
object PocketBaseConfig {
    // Current IP address (change to domain later)
    const val BASE_URL = "http://103.159.51.215:8090"

    // Collections
    object Collections {
        // App Content (Landing page)
        const val FEATURES = "features"
        const val USE_CASES = "use_cases"

        // Destinations & Tours
        const val DESTINATIONS = "destinations"
        const val TOURS = "tours"
        const val TOUR_REVIEWS = "tour_reviews"

        // Users & Profile (users collection is PocketBase auth, managed separately)
        const val USER_PROFILES = "user_profiles"
        const val USER_STATS = "user_stats"
        const val ACHIEVEMENTS = "achievements"
        const val USER_ACHIEVEMENTS = "user_achievements"

        // Trips
        const val TRIPS = "trips"
        const val TRIP_MEMBERS = "trip_members"
        const val TRIP_EXPENSES = "trip_expenses"
        const val TRIP_CHECKLISTS = "trip_checklists"
        const val TRIP_CHECKLIST_ITEMS = "trip_checklist_items"
        const val TRIP_NOTIFICATIONS = "trip_notifications"

        // Travel Buddies
        const val BUDDY_REQUESTS = "buddy_requests"
        const val BUDDY_REQUEST_TAGS = "buddy_request_tags"

        // Blog
        const val BLOG_POSTS = "blog_posts"
        const val BLOG_CATEGORIES = "blog_categories"

        // Social Feed
        const val POSTS = "posts"
        const val POST_MEDIA = "post_media"
        const val POST_LIKES = "post_likes"
        const val POST_COMMENTS = "post_comments"

        // Notifications & Photos
        const val NOTIFICATIONS = "notifications"
        const val TRIP_PHOTOS = "trip_photos"

        // Legacy (to be migrated)
        const val HOTELS = "hotels"
        const val REVIEWS = "reviews"
    }

    // Admin credentials for auto collection setup
    // TODO: Move to secure storage in production!
    object Admin {
        const val EMAIL = "binhnt1@gmail.com"
        const val PASSWORD = "A1a@a#a\$a%"
    }
}
