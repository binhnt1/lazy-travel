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
            println("🔍 Trying to load: $fileName")

            // Debug: List available resources
            val loader = ResourceLoader::class.java.classLoader
            println("🔍 ClassLoader: $loader")

            // Try multiple resource loading strategies
            val inputStream =
                // Strategy 1: Load from root of resources (commonMain/resources)
                ResourceLoader::class.java.getResourceAsStream("/$fileName")?.also {
                    println("✅ Found via strategy 1: /$fileName")
                }
                // Strategy 2: Load without leading slash
                ?: ResourceLoader::class.java.getResourceAsStream(fileName)?.also {
                    println("✅ Found via strategy 2: $fileName")
                }
                // Strategy 3: Use classLoader
                ?: ResourceLoader::class.java.classLoader?.getResourceAsStream(fileName)?.also {
                    println("✅ Found via strategy 3 (classLoader): $fileName")
                }
                // Strategy 4: Try with file:// prefix (sometimes needed)
                ?: ResourceLoader::class.java.classLoader?.getResourceAsStream("file://$fileName")?.also {
                    println("✅ Found via strategy 4: file://$fileName")
                }
                ?: run {
                    println("❌ All strategies failed for: $fileName")
                    throw IllegalArgumentException("File not found in resources: $fileName")
                }

            inputStream.bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            println("❌ Failed to load $fileName on Android: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }
}
