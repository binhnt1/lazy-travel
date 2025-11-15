package com.lazytravel

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.ComposeUIViewController
import com.lazytravel.data.remote.PocketBaseClient
import com.lazytravel.data.setup.SetupFeaturesCollection
import com.lazytravel.ui.screens.HomeNoAuthScreen
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import platform.UIKit.UIViewController

/**
 * iOS Entry Point - Creates UIViewController with Compose content
 *
 * This function is called from SwiftUI to display the Home-noauth screen
 * with HeroSection and other components.
 *
 * Note: Configure enforceStrictPlistSanityCheck = false to disable
 * CADisableMinimumFrameDurationOnPhone check (already added to Info.plist)
 */
fun MainViewController(): UIViewController {
    // Initialize PocketBase client (only once)
    PocketBaseClient.initialize()
    println("✅ PocketBase client initialized for iOS")

    // Run production data setup (one-time execution)
    // TODO: Remove after first successful run
    GlobalScope.launch {
        println("\n🔧 Running one-time setup for production data...")
        val result = SetupFeaturesCollection.setup()
        result.fold(
            onSuccess = { message -> println("$message\n") },
            onFailure = { error -> println("❌ Setup error: ${error.message}\n") }
        )
    }

    return ComposeUIViewController(
        configure = {
            // Disable strict plist check - we've already added the key to Info.plist
            // This prevents crash on iPhones with ProMotion displays
            enforceStrictPlistSanityCheck = false
        }
    ) {
        HomeNoAuthScreen()
    }
}
