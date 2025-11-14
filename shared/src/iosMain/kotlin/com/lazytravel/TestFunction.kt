package com.lazytravel

/**
 * Simple test function to verify iOS framework is working
 * Does NOT use Compose - just basic Kotlin
 */
fun getTestMessage(): String {
    return "✅ Kotlin/Native framework is working on iOS!"
}

/**
 * Test function with parameter
 */
fun greet(name: String): String {
    return "Hello from Kotlin, $name!"
}
