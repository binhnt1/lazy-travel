package com.lazytravel.core.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

/**
 * Composable helper to get localized string
 * Auto-recomposes when language changes
 *
 * Usage: localizedString("hero_title")
 */
@Composable
fun localizedString(key: String): String {
    val currentLanguage by LocalizationManager.currentLanguage.collectAsState()
    // Trigger recomposition when language changes
    return LocalizationManager.getString(key)
}
