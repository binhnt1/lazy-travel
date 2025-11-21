package com.lazytravel.ui.components.atoms

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitViewController
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.*
import platform.AVKit.AVPlayerViewController
import platform.Foundation.NSURL

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun VideoPlayer(
    url: String,
    modifier: Modifier,
    autoPlay: Boolean,
    showControls: Boolean,
    isMuted: Boolean,
    isFullscreen: Boolean,
    onMuteToggle: (() -> Unit)?,
    onFullscreenToggle: (() -> Unit)?
) {
    val player = remember(url) {
        val nsUrl = NSURL.URLWithString(url)
        AVPlayer(uRL = nsUrl!!).apply {
            if (autoPlay) {
                play()
            }
        }
    }

    // Apply mute state
    LaunchedEffect(isMuted) {
        player.muted = isMuted
    }

    DisposableEffect(Unit) {
        onDispose {
            player.pause()
        }
    }

    // Use UIKitViewController to embed AVPlayerViewController
    UIKitViewController(
        factory = {
            val playerViewController = AVPlayerViewController()
            playerViewController.player = player
            playerViewController.showsPlaybackControls = showControls
            playerViewController
        },
        modifier = modifier,
        update = { playerViewController ->
            // Update player when URL changes
            playerViewController.player = player
            playerViewController.showsPlaybackControls = showControls

            // Note: iOS AVPlayerViewController handles fullscreen automatically
            // through its built-in controls, so we don't need to manually handle it
        }
    )
}
