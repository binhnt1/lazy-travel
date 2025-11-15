package com.lazytravel.core.i18n

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Localization Manager
 * Manages current language and loads translations from JSON files
 */
object LocalizationManager {
    private val _currentLanguage = MutableStateFlow(Language.VIETNAMESE)
    val currentLanguage: StateFlow<Language> = _currentLanguage.asStateFlow()

    // Cache for loaded translations
    private val translationsCache = mutableMapOf<Language, Map<String, String>>()

    private val json = Json { ignoreUnknownKeys = true }

    init {
        // Pre-load default language translations
        loadTranslations(Language.VIETNAMESE)
        loadTranslations(Language.ENGLISH)
    }

    /**
     * Load translations from JSON file
     */
    private fun loadTranslations(language: Language) {
        if (translationsCache.containsKey(language)) return

        try {
            // For now, use hardcoded translations until resource loading is implemented
            // TODO: Load from actual JSON files when resource loading is available
            val translations = getHardcodedTranslations(language)
            translationsCache[language] = translations
            println("✅ Loaded translations for ${language.displayName}")
        } catch (e: Exception) {
            println("❌ Failed to load translations for ${language.displayName}: ${e.message}")
            translationsCache[language] = emptyMap()
        }
    }

    /**
     * Temporary hardcoded translations
     * TODO: Replace with actual JSON file loading
     */
    private fun getHardcodedTranslations(language: Language): Map<String, String> {
        return when (language) {
            Language.VIETNAMESE -> mapOf(
                "app_name" to "LazyTravel",
                "hero_title" to "Lên kế hoạch du lịch dễ dàng cùng bạn bè",
                "hero_subtitle" to "Vote dân chủ, chia chi phí công bằng, và tận hưởng chuyến đi nhóm không rắc rối",
                "get_started_free" to "Bắt đầu miễn phí",
                "login" to "Đăng nhập",
                "stats_users" to "Người dùng",
                "stats_trips" to "Chuyến đi",
                "stats_destinations" to "Điểm đến",
                "stats_rating" to "Đánh giá",
                "features_title" to "Tại sao chọn TravelVote?",
                "feature_voting" to "Vote dân chủ",
                "feature_voting_desc" to "Mọi người vote điểm đến, khách sạn & hoạt động",
                "feature_cost_splitting" to "Chia chi phí thông minh",
                "feature_cost_splitting_desc" to "Tự động tính toán và chia chi phí công bằng",
                "feature_ai_itinerary" to "Lịch trình AI",
                "feature_ai_itinerary_desc" to "Tạo kế hoạch tối ưu theo từng ngày",
                "feature_shared_album" to "Album chung",
                "feature_shared_album_desc" to "Lưu và chia sẻ ảnh cùng nhóm bạn"
            )
            Language.ENGLISH -> mapOf(
                "app_name" to "LazyTravel",
                "hero_title" to "Plan your trip easily with friends",
                "hero_subtitle" to "Vote democratically, split costs fairly, and enjoy hassle-free group travel",
                "get_started_free" to "Get Started Free",
                "login" to "Login",
                "stats_users" to "Users",
                "stats_trips" to "Trips",
                "stats_destinations" to "Destinations",
                "stats_rating" to "Rating",
                "features_title" to "Why Choose TravelVote?",
                "feature_voting" to "Democratic Voting",
                "feature_voting_desc" to "Everyone votes on destinations, hotels & activities",
                "feature_cost_splitting" to "Smart Cost Splitting",
                "feature_cost_splitting_desc" to "Auto-calculate and split expenses fairly",
                "feature_ai_itinerary" to "AI Itinerary",
                "feature_ai_itinerary_desc" to "Generate optimized day-by-day plans",
                "feature_shared_album" to "Shared Album",
                "feature_shared_album_desc" to "Save and share photos with group"
            )
        }
    }

    /**
     * Switch to a different language
     */
    fun setLanguage(language: Language) {
        loadTranslations(language)  // Ensure translations are loaded
        _currentLanguage.value = language
    }

    /**
     * Get localized string by key
     * Returns the key itself if translation not found
     */
    fun getString(key: String): String {
        val translations = translationsCache[_currentLanguage.value] ?: emptyMap()
        return translations[key] ?: run {
            println("⚠️ Translation missing: $key for ${_currentLanguage.value.code}")
            key  // Return key as fallback
        }
    }
}
