package com.lazytravel.data.remote

import com.lazytravel.domain.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

/**
 * Complete Sample Data Seeder
 * Seeds all collections with realistic test data
 */
object CompleteSeedData {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }

    // Store created IDs for relationships
    private val createdDestinationIds = mutableListOf<String>()
    private val createdTourIds = mutableListOf<String>()
    private val createdTripIds = mutableListOf<String>()
    private val createdPostIds = mutableListOf<String>()
    private val createdBlogCategoryIds = mutableListOf<String>()
    private val createdUserIds = mutableListOf<String>()

    /**
     * Seed ALL data - Complete setup for testing
     */
    suspend fun seedAll() {
        println("🌱 Starting complete data seeding...")
        println("=" .repeat(50))

        // Seed in order (respecting dependencies)
        seedDestinations()
        seedBlogCategories()
        seedTours()
        seedAchievements()
        seedBuddyRequests()

        println("=" .repeat(50))
        println("✅ Complete seeding finished!")
        println("💡 You can now use the app with sample data")
    }

    /**
     * Seed Destinations
     */
    suspend fun seedDestinations() = withContext(Dispatchers.Default) {
        try {
            println("\n📍 Seeding Destinations...")

            val destinations = listOf(
                Destination(
                    name = "Ha Long Bay",
                    description = "UNESCO World Heritage Site featuring thousands of limestone islands and islets in emerald waters",
                    imageUrl = "https://images.unsplash.com/photo-1528127269322-539801943592",
                    rating = 4.8,
                    price = 1200000.0,
                    location = "Quang Ninh, Vietnam",
                    category = "Nature"
                ),
                Destination(
                    name = "Hoi An Ancient Town",
                    description = "Well-preserved ancient town with unique architecture, lanterns, and vibrant culture",
                    imageUrl = "https://images.unsplash.com/photo-1583417319070-4a69db38a482",
                    rating = 4.7,
                    price = 800000.0,
                    location = "Quang Nam, Vietnam",
                    category = "Cultural"
                ),
                Destination(
                    name = "Sapa Terraced Fields",
                    description = "Stunning rice terraces and ethnic minority villages in the Hoang Lien Son mountains",
                    imageUrl = "https://images.unsplash.com/photo-1559592413-7cec4d0cae2b",
                    rating = 4.6,
                    price = 1500000.0,
                    location = "Lao Cai, Vietnam",
                    category = "Adventure"
                ),
                Destination(
                    name = "Phu Quoc Island",
                    description = "Tropical paradise with pristine beaches, coral reefs, and crystal clear waters",
                    imageUrl = "https://images.unsplash.com/photo-1552465011-b4e21bf6e79a",
                    rating = 4.5,
                    price = 2000000.0,
                    location = "Kien Giang, Vietnam",
                    category = "Beach"
                ),
                Destination(
                    name = "Da Lat City",
                    description = "Cool mountain city known for flowers, coffee plantations, and French colonial architecture",
                    imageUrl = "https://images.unsplash.com/photo-1583417319070-4a69db38a482",
                    rating = 4.4,
                    price = 900000.0,
                    location = "Lam Dong, Vietnam",
                    category = "Mountain"
                ),
                Destination(
                    name = "Ninh Binh",
                    description = "Halong Bay on land - stunning karst landscapes, rice paddies, and ancient temples",
                    imageUrl = "https://images.unsplash.com/photo-1583417319070-4a69db38a482",
                    rating = 4.6,
                    price = 700000.0,
                    location = "Ninh Binh, Vietnam",
                    category = "Nature"
                ),
                Destination(
                    name = "Nha Trang Beach",
                    description = "Famous coastal city with beautiful beaches, diving spots, and vibrant nightlife",
                    imageUrl = "https://images.unsplash.com/photo-1583417319070-4a69db38a482",
                    rating = 4.3,
                    price = 1000000.0,
                    location = "Khanh Hoa, Vietnam",
                    category = "Beach"
                ),
                Destination(
                    name = "Phong Nha Cave",
                    description = "UNESCO World Heritage Site with the world's largest caves and underground rivers",
                    imageUrl = "https://images.unsplash.com/photo-1583417319070-4a69db38a482",
                    rating = 4.9,
                    price = 1800000.0,
                    location = "Quang Binh, Vietnam",
                    category = "Adventure"
                )
            )

            var successCount = 0
            createdDestinationIds.clear()

            for (destination in destinations) {
                val result = PocketBaseApi.createRecord(
                    PocketBaseConfig.Collections.DESTINATIONS,
                    destination
                )

                result.fold(
                    onSuccess = { responseText ->
                        try {
                            val created = json.decodeFromString(Destination.serializer(), responseText)
                            createdDestinationIds.add(created.id)
                            println("  ✅ ${destination.name}")
                            successCount++
                        } catch (e: Exception) {
                            println("  ⚠️ Created but failed to parse: ${destination.name}")
                        }
                    },
                    onFailure = { error ->
                        println("  ❌ ${destination.name}: ${error.message}")
                    }
                )
            }

            println("📍 Destinations: $successCount/${destinations.size} created")

        } catch (e: Exception) {
            println("❌ Destination seeding failed: ${e.message}")
        }
    }

    /**
     * Seed Tours
     */
    suspend fun seedTours() = withContext(Dispatchers.Default) {
        try {
            println("\n🎫 Seeding Tours...")

            if (createdDestinationIds.isEmpty()) {
                println("  ⚠️ No destinations found, skipping tours")
                return@withContext
            }

            val tours = listOf(
                Tour(
                    name = "Ha Long Bay Luxury Cruise - 2D1N",
                    destinationId = createdDestinationIds[0],
                    pricePerPerson = 3500000.0,
                    durationDays = 2,
                    description = "Luxury overnight cruise with kayaking, cave exploration, and fresh seafood",
                    highlights = listOf("Luxury cabin", "Kayaking", "Cave visit", "Tai Chi session", "Fresh seafood"),
                    included = listOf("Accommodation", "All meals", "Activities", "English guide"),
                    excluded = listOf("Drinks", "Tips", "Personal expenses"),
                    badge = TourBadge.HOT,
                    rating = 4.8,
                    reviewsCount = 234,
                    maxParticipants = 20,
                    availableSlots = 8,
                    isActive = true
                ),
                Tour(
                    name = "Hoi An Walking & Cooking Tour",
                    destinationId = createdDestinationIds.getOrNull(1) ?: createdDestinationIds[0],
                    pricePerPerson = 950000.0,
                    durationDays = 1,
                    description = "Explore ancient town and learn to cook authentic Vietnamese dishes",
                    highlights = listOf("Ancient town tour", "Cooking class", "Market visit", "Local lunch"),
                    included = listOf("English guide", "Cooking class", "Meals", "Market tour"),
                    excluded = listOf("Hotel pickup", "Personal shopping"),
                    badge = TourBadge.NEW,
                    rating = 4.7,
                    reviewsCount = 156,
                    maxParticipants = 12,
                    availableSlots = 5,
                    isActive = true
                ),
                Tour(
                    name = "Sapa Trekking Adventure - 3D2N",
                    destinationId = createdDestinationIds.getOrNull(2) ?: createdDestinationIds[0],
                    pricePerPerson = 2800000.0,
                    durationDays = 3,
                    description = "Trek through rice terraces and stay with local families",
                    highlights = listOf("Rice terrace trekking", "Homestay experience", "Local villages", "Mountain views"),
                    included = listOf("Homestay", "All meals", "Trekking guide", "Transportation"),
                    excluded = listOf("Travel insurance", "Personal expenses"),
                    badge = TourBadge.DISCOUNT,
                    rating = 4.6,
                    reviewsCount = 89,
                    maxParticipants = 15,
                    availableSlots = 10,
                    isActive = true
                ),
                Tour(
                    name = "Phu Quoc Island Hopping",
                    destinationId = createdDestinationIds.getOrNull(3) ?: createdDestinationIds[0],
                    pricePerPerson = 1200000.0,
                    durationDays = 1,
                    description = "Visit beautiful islands, snorkeling, and beach BBQ lunch",
                    highlights = listOf("3 islands visit", "Snorkeling", "BBQ lunch", "Sunset view"),
                    included = listOf("Boat transfer", "Snorkeling gear", "Lunch", "Guide"),
                    excluded = listOf("Drinks", "Water activities"),
                    rating = 4.5,
                    reviewsCount = 312,
                    maxParticipants = 30,
                    availableSlots = 18,
                    isActive = true
                ),
                Tour(
                    name = "Phong Nha Cave Exploration",
                    destinationId = createdDestinationIds.getOrNull(7) ?: createdDestinationIds[0],
                    pricePerPerson = 2500000.0,
                    durationDays = 1,
                    description = "Explore Paradise Cave and Dark Cave with zip-lining adventure",
                    highlights = listOf("Paradise Cave", "Dark Cave", "Zip-lining", "Mud bath", "Cave swimming"),
                    included = listOf("Cave tickets", "Equipment", "Lunch", "Guide", "Transportation"),
                    excluded = listOf("Personal insurance", "Drinks"),
                    badge = TourBadge.HOT,
                    rating = 4.9,
                    reviewsCount = 445,
                    maxParticipants = 25,
                    availableSlots = 12,
                    isActive = true
                )
            )

            var successCount = 0
            createdTourIds.clear()

            for (tour in tours) {
                val result = PocketBaseApi.createRecord(
                    PocketBaseConfig.Collections.TOURS,
                    tour
                )

                result.fold(
                    onSuccess = { responseText ->
                        try {
                            val created = json.decodeFromString(Tour.serializer(), responseText)
                            createdTourIds.add(created.id)
                            println("  ✅ ${tour.name}")
                            successCount++
                        } catch (e: Exception) {
                            println("  ⚠️ Created but failed to parse: ${tour.name}")
                        }
                    },
                    onFailure = { error ->
                        println("  ❌ ${tour.name}: ${error.message}")
                    }
                )
            }

            println("🎫 Tours: $successCount/${tours.size} created")

        } catch (e: Exception) {
            println("❌ Tour seeding failed: ${e.message}")
        }
    }

    /**
     * Seed Blog Categories
     */
    suspend fun seedBlogCategories() = withContext(Dispatchers.Default) {
        try {
            println("\n📚 Seeding Blog Categories...")

            val categories = listOf(
                BlogCategory(
                    name = "Travel Tips",
                    icon = "💡",
                    color = "#4CAF50",
                    isActive = true
                ),
                BlogCategory(
                    name = "Destination Guides",
                    icon = "🗺️",
                    color = "#2196F3",
                    isActive = true
                ),
                BlogCategory(
                    name = "Food & Culture",
                    icon = "🍜",
                    color = "#FF9800",
                    isActive = true
                ),
                BlogCategory(
                    name = "Adventure Stories",
                    icon = "⛰️",
                    color = "#F44336",
                    isActive = true
                ),
                BlogCategory(
                    name = "Budget Travel",
                    icon = "💰",
                    color = "#9C27B0",
                    isActive = true
                )
            )

            var successCount = 0
            createdBlogCategoryIds.clear()

            for (category in categories) {
                val result = PocketBaseApi.createRecord(
                    PocketBaseConfig.Collections.BLOG_CATEGORIES,
                    category
                )

                result.fold(
                    onSuccess = { responseText ->
                        try {
                            val created = json.decodeFromString(BlogCategory.serializer(), responseText)
                            createdBlogCategoryIds.add(created.id)
                            println("  ✅ ${category.name}")
                            successCount++
                        } catch (e: Exception) {
                            println("  ⚠️ Created but failed to parse: ${category.name}")
                        }
                    },
                    onFailure = { error ->
                        println("  ❌ ${category.name}: ${error.message}")
                    }
                )
            }

            println("📚 Blog Categories: $successCount/${categories.size} created")

            // Seed blog posts if categories created successfully
            if (createdBlogCategoryIds.isNotEmpty()) {
                seedBlogPosts()
            }

        } catch (e: Exception) {
            println("❌ Blog category seeding failed: ${e.message}")
        }
    }

    /**
     * Seed Blog Posts
     */
    private suspend fun seedBlogPosts() {
        try {
            println("\n📝 Seeding Blog Posts...")

            val posts = listOf(
                BlogPost(
                    authorId = "demo_user_1",
                    categoryId = createdBlogCategoryIds[0],
                    title = "10 Essential Tips for First-Time Travelers to Vietnam",
                    excerpt = "Planning your first trip to Vietnam? Here are the essential tips you need to know before you go.",
                    content = "<h2>Essential Tips</h2><p>Vietnam is an incredible destination with rich culture, delicious food, and stunning landscapes. Here are 10 tips to make your trip unforgettable...</p>",
                    readTimeMinutes = 8,
                    viewsCount = 1234,
                    likesCount = 89,
                    isPublished = true,
                    publishedAt = "2024-01-15T10:00:00Z",
                    tags = listOf("vietnam", "travel-tips", "first-time", "guide")
                ),
                BlogPost(
                    authorId = "demo_user_2",
                    categoryId = createdBlogCategoryIds.getOrNull(1) ?: createdBlogCategoryIds[0],
                    title = "The Ultimate Guide to Ha Long Bay: Everything You Need to Know",
                    excerpt = "Discover the best time to visit, what to pack, and how to choose the perfect cruise for your Ha Long Bay adventure.",
                    content = "<h2>Why Ha Long Bay</h2><p>Ha Long Bay is one of Vietnam's most iconic destinations. With over 1,600 limestone islands...</p>",
                    readTimeMinutes = 12,
                    viewsCount = 2156,
                    likesCount = 156,
                    isPublished = true,
                    publishedAt = "2024-01-20T14:30:00Z",
                    tags = listOf("ha-long-bay", "cruise", "unesco", "nature")
                ),
                BlogPost(
                    authorId = "demo_user_1",
                    categoryId = createdBlogCategoryIds.getOrNull(2) ?: createdBlogCategoryIds[0],
                    title = "Street Food Heaven: Must-Try Dishes in Hanoi",
                    excerpt = "From pho to banh mi, explore the best street food that Hanoi has to offer.",
                    content = "<h2>Hanoi Street Food</h2><p>Hanoi is a paradise for food lovers. The street food scene is vibrant, diverse, and absolutely delicious...</p>",
                    readTimeMinutes = 10,
                    viewsCount = 3421,
                    likesCount = 234,
                    isPublished = true,
                    publishedAt = "2024-02-01T09:00:00Z",
                    tags = listOf("food", "hanoi", "street-food", "culture")
                ),
                BlogPost(
                    authorId = "demo_user_3",
                    categoryId = createdBlogCategoryIds.getOrNull(3) ?: createdBlogCategoryIds[0],
                    title = "Trekking Sapa: A Journey Through Vietnam's Rice Terraces",
                    excerpt = "My 3-day trekking experience in Sapa, from Cat Cat village to the summit of Fansipan.",
                    content = "<h2>Day 1: Cat Cat Village</h2><p>The journey began early morning as we set out from Sapa town towards Cat Cat village...</p>",
                    readTimeMinutes = 15,
                    viewsCount = 1876,
                    likesCount = 123,
                    isPublished = true,
                    publishedAt = "2024-02-10T16:00:00Z",
                    tags = listOf("sapa", "trekking", "adventure", "mountains")
                ),
                BlogPost(
                    authorId = "demo_user_2",
                    categoryId = createdBlogCategoryIds.getOrNull(4) ?: createdBlogCategoryIds[0],
                    title = "How I Traveled Vietnam for Under $30/Day",
                    excerpt = "A comprehensive budget breakdown and tips for traveling Vietnam on a shoestring budget.",
                    content = "<h2>Budget Breakdown</h2><p>Vietnam is one of the most budget-friendly countries in Southeast Asia. Here's how I managed to travel comfortably...</p>",
                    readTimeMinutes = 9,
                    viewsCount = 4532,
                    likesCount = 312,
                    isPublished = true,
                    publishedAt = "2024-02-15T11:00:00Z",
                    tags = listOf("budget-travel", "backpacking", "money-saving", "tips")
                )
            )

            var successCount = 0

            for (post in posts) {
                val result = PocketBaseApi.createRecord(
                    PocketBaseConfig.Collections.BLOG_POSTS,
                    post
                )

                result.fold(
                    onSuccess = {
                        println("  ✅ ${post.title}")
                        successCount++
                    },
                    onFailure = { error ->
                        println("  ❌ ${post.title}: ${error.message}")
                    }
                )
            }

            println("📝 Blog Posts: $successCount/${posts.size} created")

        } catch (e: Exception) {
            println("❌ Blog post seeding failed: ${e.message}")
        }
    }

    /**
     * Seed Buddy Requests
     */
    suspend fun seedBuddyRequests() = withContext(Dispatchers.Default) {
        try {
            println("\n🤝 Seeding Buddy Requests...")

            if (createdDestinationIds.isEmpty()) {
                println("  ⚠️ No destinations found, skipping buddy requests")
                return@withContext
            }

            val requests = listOf(
                BuddyRequest(
                    createdBy = "demo_user_1",
                    tripName = "Ha Long Bay Weekend Getaway",
                    destinationId = createdDestinationIds[0],
                    startDate = "2024-12-20",
                    endDate = "2024-12-22",
                    totalSlots = 4,
                    filledSlots = 2,
                    pricePerPerson = 2500000.0,
                    description = "Looking for 2 more people to join our Ha Long Bay cruise adventure! We've booked a luxury cruise and want to split the costs.",
                    requirements = "Easy-going, love adventure, age 25-40",
                    tags = listOf("cruise", "weekend", "luxury", "nature"),
                    status = BuddyRequestStatus.OPEN
                ),
                BuddyRequest(
                    createdBy = "demo_user_2",
                    tripName = "Sapa Trekking Expedition",
                    destinationId = createdDestinationIds.getOrNull(2) ?: createdDestinationIds[0],
                    startDate = "2024-12-28",
                    endDate = "2024-12-31",
                    totalSlots = 6,
                    filledSlots = 4,
                    pricePerPerson = 1800000.0,
                    description = "3-day trekking through rice terraces, staying in homestays. Need 2 more adventurous souls!",
                    requirements = "Good physical fitness, hiking experience preferred",
                    tags = listOf("trekking", "adventure", "mountains", "homestay"),
                    status = BuddyRequestStatus.OPEN
                ),
                BuddyRequest(
                    createdBy = "demo_user_3",
                    tripName = "Phu Quoc Beach Holiday",
                    destinationId = createdDestinationIds.getOrNull(3) ?: createdDestinationIds[0],
                    startDate = "2025-01-05",
                    endDate = "2025-01-10",
                    totalSlots = 4,
                    filledSlots = 1,
                    pricePerPerson = 3500000.0,
                    description = "Looking for beach lovers to share a beachfront villa in Phu Quoc! Relaxation, snorkeling, and good vibes.",
                    requirements = "Love beaches, diving/snorkeling enthusiasts welcome",
                    tags = listOf("beach", "diving", "relaxation", "villa"),
                    status = BuddyRequestStatus.OPEN
                ),
                BuddyRequest(
                    createdBy = "demo_user_4",
                    tripName = "Hoi An Cultural Tour",
                    destinationId = createdDestinationIds.getOrNull(1) ?: createdDestinationIds[0],
                    startDate = "2025-01-15",
                    endDate = "2025-01-18",
                    totalSlots = 4,
                    filledSlots = 4,
                    description = "FULL - Ancient town exploration, lantern festival, and cooking classes.",
                    tags = listOf("culture", "food", "photography"),
                    status = BuddyRequestStatus.FULL
                )
            )

            var successCount = 0

            for (request in requests) {
                val result = PocketBaseApi.createRecord(
                    PocketBaseConfig.Collections.BUDDY_REQUESTS,
                    request
                )

                result.fold(
                    onSuccess = {
                        println("  ✅ ${request.tripName}")
                        successCount++
                    },
                    onFailure = { error ->
                        println("  ❌ ${request.tripName}: ${error.message}")
                    }
                )
            }

            println("🤝 Buddy Requests: $successCount/${requests.size} created")

        } catch (e: Exception) {
            println("❌ Buddy request seeding failed: ${e.message}")
        }
    }

    /**
     * Seed Achievements
     */
    suspend fun seedAchievements() = withContext(Dispatchers.Default) {
        try {
            println("\n🏆 Seeding Achievements...")

            val achievements = listOf(
                Achievement(
                    name = "First Trip",
                    description = "Complete your first trip",
                    icon = "🎉",
                    category = AchievementCategory.TRIPS,
                    pointsReward = 100,
                    requirement = "Complete 1 trip",
                    isActive = true
                ),
                Achievement(
                    name = "Explorer",
                    description = "Visit 5 different destinations",
                    icon = "🗺️",
                    category = AchievementCategory.EXPLORER,
                    pointsReward = 250,
                    requirement = "Visit 5 destinations",
                    isActive = true
                ),
                Achievement(
                    name = "Social Butterfly",
                    description = "Create 10 posts",
                    icon = "🦋",
                    category = AchievementCategory.SOCIAL,
                    pointsReward = 150,
                    requirement = "Create 10 posts",
                    isActive = true
                ),
                Achievement(
                    name = "Travel Planner",
                    description = "Plan 3 trips successfully",
                    icon = "📋",
                    category = AchievementCategory.TRIPS,
                    pointsReward = 200,
                    requirement = "Plan 3 trips",
                    isActive = true
                ),
                Achievement(
                    name = "Helpful Reviewer",
                    description = "Write 5 helpful reviews",
                    icon = "⭐",
                    category = AchievementCategory.COMMUNITY,
                    pointsReward = 150,
                    requirement = "Write 5 reviews",
                    isActive = true
                ),
                Achievement(
                    name = "Budget Master",
                    description = "Complete a trip under budget",
                    icon = "💰",
                    category = AchievementCategory.TRIPS,
                    pointsReward = 300,
                    requirement = "Stay under budget on a trip",
                    isActive = true
                ),
                Achievement(
                    name = "Photographer",
                    description = "Upload 50 photos",
                    icon = "📸",
                    category = AchievementCategory.SOCIAL,
                    pointsReward = 200,
                    requirement = "Upload 50 photos",
                    isActive = true
                ),
                Achievement(
                    name = "Adventure Seeker",
                    description = "Complete 3 adventure activities",
                    icon = "🏔️",
                    category = AchievementCategory.SPECIAL,
                    pointsReward = 500,
                    requirement = "Complete 3 adventure activities",
                    isActive = true
                )
            )

            var successCount = 0

            for (achievement in achievements) {
                val result = PocketBaseApi.createRecord(
                    PocketBaseConfig.Collections.ACHIEVEMENTS,
                    achievement
                )

                result.fold(
                    onSuccess = {
                        println("  ✅ ${achievement.name}")
                        successCount++
                    },
                    onFailure = { error ->
                        println("  ❌ ${achievement.name}: ${error.message}")
                    }
                )
            }

            println("🏆 Achievements: $successCount/${achievements.size} created")

        } catch (e: Exception) {
            println("❌ Achievement seeding failed: ${e.message}")
        }
    }

    /**
     * Clear all seeded data (DANGEROUS!)
     */
    suspend fun clearAll() {
        println("🗑️ WARNING: Clearing all data...")
        println("This will delete ALL records from all collections!")

        // Clear in reverse order to avoid FK issues
        clearCollection(PocketBaseConfig.Collections.ACHIEVEMENTS, "Achievements")
        clearCollection(PocketBaseConfig.Collections.BUDDY_REQUESTS, "Buddy Requests")
        clearCollection(PocketBaseConfig.Collections.BLOG_POSTS, "Blog Posts")
        clearCollection(PocketBaseConfig.Collections.BLOG_CATEGORIES, "Blog Categories")
        clearCollection(PocketBaseConfig.Collections.TOUR_REVIEWS, "Tour Reviews")
        clearCollection(PocketBaseConfig.Collections.TOURS, "Tours")
        clearCollection(PocketBaseConfig.Collections.DESTINATIONS, "Destinations")

        println("🗑️ Clear complete!")
    }

    private suspend fun clearCollection(collectionName: String, displayName: String) {
        try {
            val result = PocketBaseApi.getRecords(
                collection = collectionName,
                perPage = 500
            )

            result.fold(
                onSuccess = { response ->
                    var deletedCount = 0
                    response.items.forEach { item ->
                        val id = item.toString().substringAfter("\"id\":\"").substringBefore("\"")
                        val deleteResult = PocketBaseApi.deleteRecord(collectionName, id)
                        if (deleteResult.isSuccess) deletedCount++
                    }
                    if (deletedCount > 0) {
                        println("  🗑️ $displayName: $deletedCount deleted")
                    }
                },
                onFailure = { }
            )
        } catch (e: Exception) {
            // Ignore errors
        }
    }
}
