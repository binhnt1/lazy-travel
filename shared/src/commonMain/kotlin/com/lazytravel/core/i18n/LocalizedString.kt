package com.lazytravel.core.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

/**
 * Composable helper to get localized string
 * Auto-recomposes when language changes
 */
@Composable
fun localizedString(key: StringKey): String {
    val currentLanguage by LocalizationManager.currentLanguage.collectAsState()
    return when (currentLanguage) {
        Language.VIETNAMESE -> key.vi
        Language.ENGLISH -> key.en
    }
}

/**
 * Non-composable version for ViewModel/Repository
 */
fun StringKey.localized(): String {
    return LocalizationManager.getString(this)
}
