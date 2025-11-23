package com.lazytravel.core.i18n

/**
 * Supported Languages
 * Add more languages here and create corresponding lang_{code}.json file
 */
enum class Language(val code: String, val displayName: String, val jsonFile: String) {
    VIETNAMESE("vi", "Tiếng Việt", "lang_vi.json"),
    ENGLISH("en", "English", "lang_en.json");

    // To add more languages (e.g., French):
    // 1. Add: FRENCH("fr", "Français", "lang_fr.json"),
    // 2. Create: shared/src/commonMain/resources/lang_fr.json
    // 3. Copy lang_en.json content and translate values
}
