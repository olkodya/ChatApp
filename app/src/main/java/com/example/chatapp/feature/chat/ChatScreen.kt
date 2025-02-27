package com.example.chatapp.feature.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun ChatScreen() {
    Box(Modifier.fillMaxSize().background(Color.Red).statusBarsPadding().navigationBarsPadding()) {
        Box(Modifier.fillMaxSize().background(Color.Green))
    }
}