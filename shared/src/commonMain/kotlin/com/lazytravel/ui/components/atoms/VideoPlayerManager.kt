package com.lazytravel.ui.components.atoms

/**
 * Global singleton to manage video playback across the app
 * Ensures only one video plays at a time
 */
object VideoPlayerManager {
    private var currentPlayingVideoId: String? = null
    private val registeredVideos = mutableMapOf<String, () -> Unit>()

    /**
     * Register a video player instance
     * @param videoId Unique identifier for the video
     * @param onPause Callback to pause this video when another starts playing
     */
    fun registerVideo(videoId: String, onPause: () -> Unit) {
        registeredVideos[videoId] = onPause
    }

    /**
     * Unregister a video player when it's disposed
     */
    fun unregisterVideo(videoId: String) {
        registeredVideos.remove(videoId)
        if (currentPlayingVideoId == videoId) {
            currentPlayingVideoId = null
        }
    }

    /**
     * Notify that a video has started playing
     * This will pause all other videos
     */
    fun onVideoStarted(videoId: String) {
        // Pause all other videos except this one
        if (currentPlayingVideoId != videoId) {
            currentPlayingVideoId?.let { currentId ->
                registeredVideos[currentId]?.invoke()
            }
            currentPlayingVideoId = videoId
        }
    }

    /**
     * Notify that a video has been paused
     */
    fun onVideoPaused(videoId: String) {
        if (currentPlayingVideoId == videoId) {
            currentPlayingVideoId = null
        }
    }
}
