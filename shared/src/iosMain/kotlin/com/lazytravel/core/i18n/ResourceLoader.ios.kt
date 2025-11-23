package com.lazytravel.core.i18n

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithContentsOfFile

/**
 * iOS implementation of ResourceLoader
 * Loads JSON files from app bundle resources
 */
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object ResourceLoader {
    @OptIn(ExperimentalForeignApi::class)
    actual fun loadJsonFile(fileName: String): String {
        return try {
            // Get main bundle
            val bundle = NSBundle.mainBundle

            // Remove .json extension to get resource name
            val resourceName = fileName.substringBeforeLast(".")
            val resourceExtension = fileName.substringAfterLast(".")

            // Get file path from bundle
            val filePath = bundle.pathForResource(resourceName, resourceExtension)
                ?: throw IllegalArgumentException("File not found in bundle: $fileName")

            // Read file content
            NSString.stringWithContentsOfFile(
                path = filePath,
                encoding = NSUTF8StringEncoding,
                error = null
            ) ?: throw IllegalArgumentException("Failed to read file: $fileName")
        } catch (e: Exception) {
            println("❌ Failed to load $fileName on iOS: ${e.message}")
            throw e
        }
    }
}
