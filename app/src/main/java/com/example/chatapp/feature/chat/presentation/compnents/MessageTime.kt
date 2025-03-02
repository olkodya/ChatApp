package com.example.chatapp.feature.chat.presentation.compnents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun MessageText(text: String) {
    Text(
        modifier = Modifier.padding(8.dp),
        text = text
    )
}


@Composable
fun MessageTime(
    color: Color,
    timestamp: Long
) {
    val messageDateTime = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(timestamp),
        ZoneId.systemDefault()
    )
    val formattedTime = messageDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(Modifier.weight(1f))
        Box(
            Modifier
                .padding(8.dp)
                .clip(CircleShape)
                .background(color),
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                text = formattedTime,
                textAlign = TextAlign.Center,
                style = TextStyle(lineHeight = 13.sp),
                fontSize = 8.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}