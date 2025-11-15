package com.lazytravel.core.i18n

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Localization Manager
 * Manages current language and provides localized strings
 */
object LocalizationManager {
    private val _currentLanguage = MutableStateFlow(Language.VIETNAMESE)
    val currentLanguage: StateFlow<Language> = _currentLanguage.asStateFlow()

    /**
     * Switch to a different language
     */
    fun setLanguage(language: Language) {
        _currentLanguage.value = language
    }

    /**
     * Get localized string by key
     */
    fun getString(key: StringKey): String {
        return when (_currentLanguage.value) {
            Language.VIETNAMESE -> key.vi
            Language.ENGLISH -> key.en
        }
    }
}

/**
 * String Key - holds both EN and VI translations
 */
data class StringKey(
    val en: String,
    val vi: String
)
