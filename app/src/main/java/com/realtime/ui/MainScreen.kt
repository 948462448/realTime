package com.realtime.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.realtime.player.PlayerManager

@Composable
fun MainScreen(playerManager: PlayerManager) {
    val context = LocalContext.current
    var showUrlInput by remember { mutableStateOf(false) }
    var urlText by remember { mutableStateOf("") }
    val playbackState by playerManager.playbackState

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            playerManager.play(it.toString())
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1A1A1A))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "RealTime",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            if (playbackState == PlayerManager.PlaybackState.IDLE) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // File picker
                    IconButton(onClick = {
                        filePickerLauncher.launch(arrayOf("video/*"))
                    }) {
                        Icon(
                            Icons.Default.FileOpen,
                            contentDescription = "打开文件",
                            tint = Color.White
                        )
                    }
                    // URL input toggle
                    IconButton(onClick = { showUrlInput = !showUrlInput }) {
                        Icon(
                            Icons.Default.Link,
                            contentDescription = "输入URL",
                            tint = Color.White
                        )
                    }
                }
            } else {
                IconButton(onClick = {
                    playerManager.stop()
                }) {
                    Icon(
                        Icons.Default.Stop,
                        contentDescription = "停止",
                        tint = Color.White
                    )
                }
            }
        }

        // URL input section
        AnimatedVisibility(visible = showUrlInput) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2A2A2A))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = urlText,
                    onValueChange = { urlText = it },
                    placeholder = { Text("粘贴视频链接", color = Color.Gray) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Button(
                    onClick = {
                        if (urlText.isNotBlank()) {
                            playerManager.play(urlText.trim())
                            showUrlInput = false
                        }
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Text("播放")
                }
            }
        }

        // Video area with subtitle overlay
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (playbackState == PlayerManager.PlaybackState.IDLE) {
                // Welcome screen
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "选择视频开始",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            Button(
                                onClick = {
                                    filePickerLauncher.launch(arrayOf("video/*"))
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.height(48.dp)
                            ) {
                                Icon(Icons.Default.FileOpen, contentDescription = null)
                                Text("本地文件", modifier = Modifier.padding(start = 8.dp))
                            }
                            Button(
                                onClick = { showUrlInput = true },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.height(48.dp)
                            ) {
                                Icon(Icons.Default.Link, contentDescription = null)
                                Text("网络视频", modifier = Modifier.padding(start = 8.dp))
                            }
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    VideoPlayer(
                        playerManager = playerManager,
                        modifier = Modifier.fillMaxSize()
                    )

                    SubtitleOverlay(
                        subtitle = null, // Will be wired in Phase 2
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }
        }
    }
}
