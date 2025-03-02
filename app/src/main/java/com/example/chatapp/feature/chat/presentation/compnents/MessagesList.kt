package com.example.chatapp.feature.chat.presentation.compnents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.chatapp.R
import com.example.chatapp.feature.chat.presentation.ChatScreenState
import com.example.chatapp.feature.chat.presentation.ChatViewModel
import com.example.chatapp.feature.chat.presentation.MessageState

@Composable
fun MessagesList(
    chatListState: LazyListState,
    paddingValues: PaddingValues,
    chatState: ChatScreenState,
    handleAction: (ChatViewModel.ChatAction) -> Unit,
) {
    LazyColumn(
        state = chatListState,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        reverseLayout = true,
        contentPadding = PaddingValues(
            top = 13.dp,
            bottom = paddingValues.calculateBottomPadding() + 8.dp
        )
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
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        MaterialTheme.colorScheme.tertiaryContainer
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
                    TextMessageItem(chatState = chatState, containerColor = containerColor)
                }

                is MessageState.MessageType.File -> {
                    FileMessageItem(chatState = chatState, containerColor = containerColor)

                }

                is MessageState.MessageType.Image -> {
                    ImageMessageItem(chatState, containerColor, shape)
                }

                is MessageState.MessageType.Video -> {
                    VideoMessageItem(chatState, containerColor)
                }

                is MessageState.MessageType.System -> {

                }
            }
        }
        if (chatState.isMeAuthor.not()) {
            Spacer(Modifier.weight(1f))
        }
    }
    if (chatState.messageType is MessageState.MessageType.System)
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                text = chatState.messageType.text.toString(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
}


@Composable
fun TextMessageItem(chatState: MessageState, containerColor: Color) {
    MessageText(text = chatState.messageType.text.toString())
    MessageTime(
        timestamp = chatState.messageTimestamp,
        color = containerColor
    )
}


@Composable
fun FileMessageItem(chatState: MessageState, containerColor: Color) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween

    ) {
        MessageIcon(
            painterResource(R.drawable.draft),
            "add"
        )
        Spacer(Modifier.width(8.dp))
        Text(
            modifier = Modifier.weight(2f),
            text = (chatState.messageType as MessageState.MessageType.File).fileName.toString()
        )
        Spacer(Modifier.width(8.dp))
        if (chatState.messageType.text == null)
            Box(Modifier.weight(1f)) {
                MessageTime(
                    timestamp = chatState.messageTimestamp,
                    color = containerColor
                )
            }
    }
    if (chatState.messageType.text != null) {

        MessageText(text = chatState.messageType.text.toString())

        MessageTime(
            timestamp = chatState.messageTimestamp,
            color = containerColor
        )
    }
}


@Composable
fun ImageMessageItem(chatState: MessageState, containerColor: Color, shape: Dp) {
    chatState.messageType as MessageState.MessageType.Image
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

@Composable
fun VideoMessageItem(chatState: MessageState, containerColor: Color) {
    chatState.messageType as MessageState.MessageType.Video

    Row(
        modifier = Modifier
            .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween

    ) {
        MessageIcon(
            painterResource(R.drawable.videocam_oultined),
            "add"
        )
        Spacer(Modifier.width(8.dp))
        Text(
            modifier = Modifier.weight(2f),
            text = chatState.messageType.videoName.toString(),
            softWrap = true,
        )
        if (chatState.messageType.text == null)
            Box(Modifier.weight(1f)) {
                MessageTime(
                    timestamp = chatState.messageTimestamp,
                    color = containerColor
                )
            }
    }
    if (chatState.messageType.text != null) {

        MessageText(text = chatState.messageType.text.toString())

        MessageTime(
            timestamp = chatState.messageTimestamp,
            color = containerColor
        )
    }
}
