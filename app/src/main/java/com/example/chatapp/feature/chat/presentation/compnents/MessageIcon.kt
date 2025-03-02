package com.example.chatapp.feature.chat.presentation.compnents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun MessageIcon(
    painter: Painter, contentDescription: String,
) {

    Box(
        Modifier
            .clip(CircleShape)
            .background(color = MaterialTheme.colorScheme.secondary)
    ) {
        Icon(
            painter = painter,
            modifier = Modifier.padding(8.dp),
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.surface
        )
    }
}