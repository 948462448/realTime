package com.realtime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.realtime.player.PlayerManager
import com.realtime.ui.MainScreen

class MainActivity : ComponentActivity() {

    private lateinit var playerManager: PlayerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        playerManager = PlayerManager(this)

        setContent {
            Surface(modifier = Modifier.fillMaxSize()) {
                MainScreen(playerManager = playerManager)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        playerManager.player.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerManager.release()
    }
}
