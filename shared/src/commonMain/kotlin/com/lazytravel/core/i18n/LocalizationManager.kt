package com.lazytravel.core.i18n

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json

/**
 * Localization Manager
 * Loads translations from JSON files (lang_*.json)
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
     * Reads from resources/lang_{code}.json
     */
    private fun loadTranslations(language: Language) {
        if (translationsCache.containsKey(language)) return

        try {
            val jsonContent = ResourceLoader.loadJsonFile(language.jsonFile)
            val translations = json.decodeFromString<Map<String, String>>(jsonContent)
            translationsCache[language] = translations
        } catch (e: Exception) {
            e.printStackTrace()
            translationsCache[language] = emptyMap()
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
