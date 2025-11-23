package com.lazytravel.ui.components.atoms

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

data class VideoPlayerState(
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val isBuffering: Boolean = false
) {
    val isEnded: Boolean
        get() = duration > 0 && currentPosition >= duration - 500 // Consider ended if within 500ms of duration

    // Override equals to compare state values properly
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is VideoPlayerState) return false
        return isPlaying == other.isPlaying &&
                currentPosition == other.currentPosition &&
                duration == other.duration &&
                isBuffering == other.isBuffering
    }

    override fun hashCode(): Int {
        var result = isPlaying.hashCode()
        result = 31 * result + currentPosition.hashCode()
        result = 31 * result + duration.hashCode()
        result = 31 * result + isBuffering.hashCode()
        return result
    }
}

@Composable
fun VideoPlayerControls(
    state: VideoPlayerState,
    onPlayPause: () -> Unit,
    onSeek: (Float) -> Unit,
    onRewind: () -> Unit,
    onFastForward: () -> Unit,
    isMuted: Boolean = false,
    onMuteToggle: (() -> Unit)? = null,
    isFullscreen: Boolean = false,
    onFullscreenToggle: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var showControls by remember { mutableStateOf(true) }

    // Auto-hide controls after 3 seconds when playing
    LaunchedEffect(showControls, state.isPlaying) {
        if (showControls && state.isPlaying) {
            delay(3000)
            showControls = false
        }
    }

    Box(modifier = modifier) {
        // Tap anywhere to show/hide controls
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    showControls = !showControls
                }
        )

        // YouTube-style gradient overlay - only at bottom
        AnimatedVisibility(
            visible = showControls || !state.isPlaying,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Center button when paused or ended
                if (!state.isPlaying) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.95f))
                            .clickable(onClick = onPlayPause),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (state.isEnded) Icons.Default.Replay else Icons.Default.PlayArrow,
                            contentDescription = if (state.isEnded) "Replay" else "Play",
                            tint = Color.Black,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                // Bottom controls bar (YouTube style)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        // Progress bar at the very bottom
                        YouTubeProgressBar(
                            currentPosition = state.currentPosition,
                            duration = state.duration,
                            onSeek = onSeek
                        )

                        // Controls row - compact and close to progress bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp, vertical = 2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Left side - Play/Pause & Time
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Play/Pause button - transparent background, white icon
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(Color.Transparent, RoundedCornerShape(4.dp))
                                        .border(1.dp, Color.White, RoundedCornerShape(4.dp))
                                        .clickable(onClick = onPlayPause),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (state.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = if (state.isPlaying) "Pause" else "Play",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                // Time display
                                Text(
                                    text = "${formatTime(state.currentPosition)} / ${formatTime(state.duration)}",
                                    fontSize = 11.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            // Right side - Rewind, Fast Forward, Mute, Fullscreen
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Rewind 10s - transparent background, white icon
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(Color.Transparent, RoundedCornerShape(4.dp))
                                        .border(1.dp, Color.White, RoundedCornerShape(4.dp))
                                        .clickable(onClick = onRewind),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FastRewind,
                                        contentDescription = "Rewind 10s",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                // Fast forward 10s - transparent background, white icon
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(Color.Transparent, RoundedCornerShape(4.dp))
                                        .border(1.dp, Color.White, RoundedCornerShape(4.dp))
                                        .clickable(onClick = onFastForward),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FastForward,
                                        contentDescription = "Fast Forward 10s",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                // Mute/Unmute button
                                if (onMuteToggle != null) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(Color.Transparent, RoundedCornerShape(4.dp))
                                            .border(1.dp, Color.White, RoundedCornerShape(4.dp))
                                            .clickable(onClick = onMuteToggle),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (isMuted) Icons.AutoMirrored.Filled.VolumeOff else Icons.AutoMirrored.Filled.VolumeUp,
                                            contentDescription = if (isMuted) "Unmute" else "Mute",
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                // Fullscreen button
                                if (onFullscreenToggle != null) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(Color.Transparent, RoundedCornerShape(4.dp))
                                            .border(1.dp, Color.White, RoundedCornerShape(4.dp))
                                            .clickable(onClick = onFullscreenToggle),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (isFullscreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                                            contentDescription = if (isFullscreen) "Exit Fullscreen" else "Fullscreen",
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Buffering indicator
        if (state.isBuffering) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}

@Composable
private fun YouTubeProgressBar(
    currentPosition: Long,
    duration: Long,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var sliderValue by remember(currentPosition, duration) {
        mutableStateOf(
            if (duration > 0) currentPosition.toFloat() / duration.toFloat() else 0f
        )
    }

    var isDragging by remember { mutableStateOf(false) }

    // Thin 3px line progress bar (white)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(20.dp),
        contentAlignment = Alignment.Center
    ) {
        // Background line (white transparent)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(Color.White.copy(alpha = 0.3f))
        )

        // Progress line (white)
        Box(
            modifier = Modifier
                .fillMaxWidth(sliderValue)
                .height(3.dp)
                .background(Color.White)
                .align(Alignment.CenterStart)
        )

        // Invisible slider for seeking
        Slider(
            value = sliderValue,
            onValueChange = { newValue ->
                sliderValue = newValue
            },
            onValueChangeFinished = {
                onSeek(sliderValue)
                isDragging = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp),
            colors = SliderDefaults.colors(
                thumbColor = if (isDragging) Color.White else Color.Transparent,
                activeTrackColor = Color.Transparent,
                inactiveTrackColor = Color.Transparent,
                activeTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent
            ),
            interactionSource = remember { MutableInteractionSource() }
                .also { interactionSource ->
                    LaunchedEffect(interactionSource) {
                        interactionSource.interactions.collect { interaction ->
                            when (interaction) {
                                is androidx.compose.foundation.interaction.DragInteraction.Start -> {
                                    isDragging = true
                                }
                                is androidx.compose.foundation.interaction.DragInteraction.Stop -> {
                                    isDragging = false
                                }
                                is androidx.compose.foundation.interaction.DragInteraction.Cancel -> {
                                    isDragging = false
                                }
                            }
                        }
                    }
                }
        )
    }
}

private fun formatTime(timeMs: Long): String {
    val totalSeconds = (timeMs / 1000).toInt()
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "$minutes:${seconds.toString().padStart(2, '0')}"
}
