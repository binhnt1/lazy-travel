package com.lazytravel.ui.components.atoms

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Multiplatform Video Player
 * - Android: Uses ExoPlayer
 * - iOS: Uses AVPlayer
 */
@Composable
expect fun VideoPlayer(
    url: String,
    modifier: Modifier = Modifier,
    autoPlay: Boolean = false,
    showControls: Boolean = true,
    isMuted: Boolean = false,
    isFullscreen: Boolean = false,
    onMuteToggle: (() -> Unit)? = null,
    onFullscreenToggle: (() -> Unit)? = null
)
