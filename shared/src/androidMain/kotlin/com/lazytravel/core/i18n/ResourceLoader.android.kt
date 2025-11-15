package com.lazytravel.core.i18n

/**
 * Android implementation of ResourceLoader
 * Loads JSON files from resources using javaClass.getResourceAsStream
 * Files must be in src/commonMain/resources/ or src/androidMain/resources/
 */
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object ResourceLoader {
    actual fun loadJsonFile(fileName: String): String {
        return try {
            // Try multiple resource loading strategies
            val inputStream =
                // Strategy 1: Load from root of resources (commonMain/resources)
                ResourceLoader::class.java.getResourceAsStream("/$fileName")
                // Strategy 2: Load without leading slash
                ?: ResourceLoader::class.java.getResourceAsStream(fileName)
                // Strategy 3: Use classLoader
                ?: ResourceLoader::class.java.classLoader?.getResourceAsStream(fileName)
                ?: throw IllegalArgumentException("File not found in resources: $fileName")

            inputStream.bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            println("❌ Failed to load $fileName on Android: ${e.message}")
            throw e
        }
    }
}
