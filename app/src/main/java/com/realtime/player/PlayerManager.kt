package com.realtime.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlayerManager(private val context: Context) {

    private val _subtitles = MutableStateFlow<List<SubtitleLine>>(emptyList())
    val subtitles: StateFlow<List<SubtitleLine>> = _subtitles.asStateFlow()

    private val _playbackState = MutableStateFlow(PlaybackState.IDLE)
    val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    val player: ExoPlayer = ExoPlayer.Builder(context).build()

    init {
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                _playbackState.value = when (state) {
                    Player.STATE_IDLE -> PlaybackState.IDLE
                    Player.STATE_BUFFERING -> PlaybackState.BUFFERING
                    Player.STATE_READY -> PlaybackState.READY
                    Player.STATE_ENDED -> PlaybackState.ENDED
                    else -> PlaybackState.IDLE
                }
            }
        })
    }

    fun play(videoUrl: String) {
        val mediaItem = MediaItem.Builder()
            .setUri(videoUrl)
            .build()
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true
    }

    fun togglePlayPause() {
        if (player.isPlaying) {
            player.pause()
        } else {
            player.play()
        }
    }

    fun seekTo(positionMs: Long) {
        player.seekTo(positionMs)
    }

    fun updateSubtitles(lines: List<SubtitleLine>) {
        _subtitles.value = lines
    }

    fun stop() {
        player.stop()
        player.clearMediaItems()
    }

    fun release() {
        player.release()
    }

    data class SubtitleLine(
        val text: String,
        val translatedText: String? = null
    )

    enum class PlaybackState {
        IDLE, BUFFERING, READY, ENDED
    }
}
