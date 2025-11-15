package com.lazytravel.core.i18n

/**
 * Android implementation of ResourceLoader
 * Loads JSON files from resources using ClassLoader
 */
actual object ResourceLoader {
    actual fun loadJsonFile(fileName: String): String {
        return try {
            // Load from resources using ClassLoader
            val classLoader = ResourceLoader::class.java.classLoader
            val inputStream = classLoader?.getResourceAsStream(fileName)
                ?: throw IllegalArgumentException("File not found: $fileName")

            inputStream.bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            println("❌ Failed to load $fileName on Android: ${e.message}")
            throw e
        }
    }
}
