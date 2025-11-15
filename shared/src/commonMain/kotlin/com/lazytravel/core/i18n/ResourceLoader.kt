package com.lazytravel.core.i18n

/**
 * Resource Loader - Platform-specific implementation
 * Loads JSON files from resources folder
 */
expect object ResourceLoader {
    /**
     * Load JSON file content from resources
     * @param fileName File name (e.g., "lang_en.json")
     * @return JSON content as String
     */
    fun loadJsonFile(fileName: String): String
}
