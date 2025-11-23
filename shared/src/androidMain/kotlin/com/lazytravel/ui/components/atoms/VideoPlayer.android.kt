package com.lazytravel.ui.components.atoms

import android.graphics.Color
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.HttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@OptIn(UnstableApi::class)
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
    val context = LocalContext.current

    // Use URL as unique video ID for the singleton manager
    val videoId = remember(url) { url }

    // Create ExoPlayer instance with custom DataSource for better compatibility
    val exoPlayer = remember(url) {
        Log.d("VideoPlayer", "Creating player for URL: '$url'")

        // Create custom HTTP data source with proper headers
        val dataSourceFactory = DefaultHttpDataSource.Factory()
            .setUserAgent("Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36")
            .setAllowCrossProtocolRedirects(true)
            .setConnectTimeoutMs(DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS)
            .setReadTimeoutMs(DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS)

        val mediaSourceFactory = DefaultMediaSourceFactory(context)
            .setDataSourceFactory(dataSourceFactory)

        ExoPlayer.Builder(context)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()
            .apply {
                val mediaItem = MediaItem.fromUri(url)
                Log.d("VideoPlayer", "MediaItem URI: ${mediaItem.localConfiguration?.uri}")
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = autoPlay
                repeatMode = Player.REPEAT_MODE_OFF

                // Add error listener for debugging
                addListener(object : Player.Listener {
                    override fun onPlayerError(error: PlaybackException) {
                        Log.e("VideoPlayer", "Playback error: ${error.message}", error)
                        Log.e("VideoPlayer", "Error code: ${error.errorCode}")
                        Log.e("VideoPlayer", "URL received: '$url'")
                        if (error.cause is HttpDataSource.InvalidResponseCodeException) {
                            val httpError = error.cause as HttpDataSource.InvalidResponseCodeException
                            Log.e("VideoPlayer", "HTTP Response code: ${httpError.responseCode}")
                            Log.e("VideoPlayer", "Attempted URL: ${httpError.dataSpec.uri}")
                        }
                    }
                })
            }
    }

    // Player state for custom controls
    var playerState by remember {
        mutableStateOf(
            VideoPlayerState(
                isPlaying = autoPlay,
                currentPosition = 0L,
                duration = 0L,
                isBuffering = false
            )
        )
    }

    // Update player state periodically - use snapshotFlow to reduce allocations
    LaunchedEffect(exoPlayer) {
        while (isActive) {
            val newState = VideoPlayerState(
                isPlaying = exoPlayer.isPlaying,
                currentPosition = exoPlayer.currentPosition.coerceAtLeast(0L),
                duration = exoPlayer.duration.coerceAtLeast(0L),
                isBuffering = exoPlayer.playbackState == Player.STATE_BUFFERING
            )
            // Only update if state actually changed to reduce recompositions
            if (playerState != newState) {
                playerState = newState
            }
            delay(250) // Update every 250ms instead of 100ms to reduce memory pressure
        }
    }

    // Register with VideoPlayerManager
    DisposableEffect(videoId) {
        // Register this video with the manager - provide pause callback
        VideoPlayerManager.registerVideo(videoId) {
            exoPlayer.pause()
        }

        onDispose {
            VideoPlayerManager.unregisterVideo(videoId)
            exoPlayer.release()
        }
    }

    // Notify manager when video starts/pauses playing
    LaunchedEffect(playerState.isPlaying) {
        if (playerState.isPlaying) {
            VideoPlayerManager.onVideoStarted(videoId)
        } else {
            VideoPlayerManager.onVideoPaused(videoId)
        }
    }

    // Update media item when URL changes
    LaunchedEffect(url) {
        val mediaItem = MediaItem.fromUri(url)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    // Apply mute state
    LaunchedEffect(isMuted) {
        exoPlayer.volume = if (isMuted) 0f else 1f
    }

    Box(modifier = modifier) {
        // AndroidView to display ExoPlayer without built-in controls
        @Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = false // Disable built-in controls

                    // Modern player styling
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                    setBackgroundColor(Color.BLACK)
                    setShowBuffering(PlayerView.SHOW_BUFFERING_NEVER) // We handle buffering in custom controls
                    keepScreenOn = true

                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            modifier = Modifier.matchParentSize(),
            update = { playerView ->
                playerView.player = exoPlayer
            }
        )

        // Custom Compose-based controls overlay
        if (showControls) {
            VideoPlayerControls(
                state = playerState,
                onPlayPause = {
                    if (exoPlayer.isPlaying) {
                        exoPlayer.pause()
                    } else {
                        // If video ended, seek to start before playing
                        if (playerState.isEnded) {
                            exoPlayer.seekTo(0)
                        }
                        exoPlayer.play()
                    }
                },
                onSeek = { fraction ->
                    val seekPosition = (exoPlayer.duration * fraction).toLong()
                    exoPlayer.seekTo(seekPosition)
                },
                onRewind = {
                    val rewindPosition = (exoPlayer.currentPosition - 10000L).coerceAtLeast(0L)
                    exoPlayer.seekTo(rewindPosition)
                },
                onFastForward = {
                    val ffPosition = (exoPlayer.currentPosition + 10000L).coerceAtMost(exoPlayer.duration)
                    exoPlayer.seekTo(ffPosition)
                },
                isMuted = isMuted,
                onMuteToggle = onMuteToggle,
                isFullscreen = isFullscreen,
                onFullscreenToggle = onFullscreenToggle,
                modifier = Modifier.matchParentSize()
            )
        }
    }
}
