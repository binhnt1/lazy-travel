package com.lazytravel.core.i18n

/**
 * Supported Languages
 * Add more languages here and create corresponding translations_{code}.json
 */
enum class Language(val code: String, val displayName: String, val jsonFile: String) {
    VIETNAMESE("vi", "Tiếng Việt", "translations_vi.json"),
    ENGLISH("en", "English", "translations_en.json");
    // To add more languages:
    // 1. Add enum value: FRENCH("fr", "Français", "translations_fr.json"),
    // 2. Create file: shared/src/commonMain/resources/translations_fr.json
    // 3. Copy translations_en.json and translate values
}
