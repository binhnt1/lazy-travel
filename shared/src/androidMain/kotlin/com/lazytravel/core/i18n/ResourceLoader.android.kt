package com.lazytravel.core.i18n

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object ResourceLoader {
    actual fun loadJsonFile(fileName: String): String {
        return try {
            println("🔍 Trying to load: $fileName")

            // Debug: List available resources
            ResourceLoader::class.java.classLoader
            val inputStream =
                ResourceLoader::class.java.getResourceAsStream("/$fileName")
                ?: ResourceLoader::class.java.getResourceAsStream(fileName)
                ?: ResourceLoader::class.java.classLoader?.getResourceAsStream(fileName)
                ?: ResourceLoader::class.java.classLoader?.getResourceAsStream("file://$fileName")
                ?: run {
                    println("❌ All strategies failed for: $fileName")
                    throw IllegalArgumentException("File not found in resources: $fileName")
                }

            inputStream.bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}
