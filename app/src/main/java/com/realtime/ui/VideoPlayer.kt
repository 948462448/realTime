package com.realtime.ui

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.realtime.player.PlayerManager

@UnstableApi
@Composable
fun VideoPlayer(
    playerManager: PlayerManager,
    modifier: Modifier = Modifier
) {
    val exoPlayer = remember { playerManager.player }

    AndroidView(
        modifier = modifier
            .fillMaxSize()
            .aspectRatio(16f / 9f),
        factory = { context ->
            PlayerView(context).apply {
                this.player = exoPlayer
                useController = true
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                setShowSubtitleButton(false)
            }
        },
        update = { view ->
            view.player = exoPlayer
        }
    )
}
