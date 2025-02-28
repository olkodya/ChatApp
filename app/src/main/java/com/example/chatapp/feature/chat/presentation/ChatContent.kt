package com.example.chatapp.feature.chat.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatContent(
    chatState: ChatScreenState,
    handleAction: (ChatViewModel.ChatAction) -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    scrolledContainerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.background,
                    actionIconContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(chatState.topBarState?.chatName.toString())
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                reverseLayout = true
            ) {
                items(
                    count = chatState.messages.size,
                    key = { id -> chatState.messages[id].id },
                ) { index ->
                    val chat = chatState.messages[index]
                    MessageItem(
                        chatState = chat,
                        handleAction = handleAction,
                    )
                }
            }
            // textField
        }
    }
}


@Composable
fun MessageItem(
    modifier: Modifier = Modifier,
    chatState: MessageState,
    handleAction: (ChatViewModel.ChatAction) -> Unit
) {
    val maxWidth: Dp = (LocalConfiguration.current.screenWidthDp * 0.6).dp
    val minWidth: Dp = (LocalConfiguration.current.screenWidthDp * 0.3).dp
    val shape: Dp = 15.dp
    val containerColor: Color = if (chatState.isMeAuthor) {
        MaterialTheme.colorScheme.secondary
    } else {
        MaterialTheme.colorScheme.tertiary
    }

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

    Row(modifier.padding(horizontal = 16.dp)) {
        if (chatState.isMeAuthor) {
            Spacer(Modifier.weight(1f))
        }
        Column(
            modifier = Modifier
                .background(
                    color = containerColor,
                    shape = RoundedCornerShape(shape)
                )
                .widthIn(
                    max = maxWidth,
                    min = minWidth,
                )
        ) {
            when (chatState.messageType) {
                is MessageState.MessageType.Text -> {
                    MessageText(text = chatState.messageType.text)
                    MessageTime(
                        timestamp = chatState.messageTimestamp,
                        color = containerColor
                    )
                }

                is MessageState.MessageType.File -> {
                    Text(chatState.messageType.fileName)
                    Text(chatState.messageType.fileUrl)
                    MessageText(text = chatState.messageType.text.toString())
                    MessageTime(
                        timestamp = chatState.messageTimestamp,
                        color = containerColor
                    )
                }

                is MessageState.MessageType.Image -> {
                    Box(
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .clip(
                                    shape = RoundedCornerShape(
                                        topStart = shape,
                                        topEnd = shape,
                                        bottomStart = if (chatState.messageType.text == null) shape else 0.dp,
                                        bottomEnd = if (chatState.messageType.text == null) shape else 0.dp
                                    )
                                )
                                .aspectRatio(chatState.messageType.ratio)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            model = chatState.messageType.imageUrl,
                            contentDescription = null,
                        )
                        if (chatState.messageType.text == null) {
                            MessageTime(
                                timestamp = chatState.messageTimestamp,
                                color = containerColor
                            )
                        }
                    }

                    chatState.messageType.text?.let { text ->
                        MessageText(text)
                    }
                    if (chatState.messageType.text != null) {
                        MessageTime(
                            timestamp = chatState.messageTimestamp,
                            color = containerColor
                        )
                    }
                }

                is MessageState.MessageType.Video -> {
                    Text(chatState.messageType.videoName)
                    Text(chatState.messageType.videoUrl)
                    MessageText(text = chatState.messageType.text.toString())
                    MessageTime(
                        timestamp = chatState.messageTimestamp,
                        color = containerColor
                    )
                }
            }

        }
        if (chatState.isMeAuthor.not()) {
            Spacer(Modifier.weight(1f))
        }
    }
}

