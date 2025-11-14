package com.lazytravel

import androidx.compose.ui.window.ComposeUIViewController
import com.lazytravel.ui.screens.HomeNoAuthScreen
import platform.UIKit.UIViewController

/**
 * iOS Entry Point - Creates UIViewController with Compose content
 *
 * This function is called from SwiftUI to display the Home-noauth screen
 * with HeroSection and other components.
 */
fun MainViewController(): UIViewController {
    return ComposeUIViewController {
        HomeNoAuthScreen()
    }
}
