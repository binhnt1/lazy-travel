package com.lazytravel

import androidx.compose.ui.window.ComposeUIViewController
import com.lazytravel.data.remote.PocketBaseClient
import com.lazytravel.ui.screens.HomeNoAuthScreen
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
