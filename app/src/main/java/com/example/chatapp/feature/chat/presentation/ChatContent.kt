package com.example.chatapp.feature.chat.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.chatapp.R
import com.example.chatapp.components.EmptyState
import com.example.chatapp.components.ErrorState
import com.example.chatapp.components.LoadingState
import com.example.chatapp.ui.theme.AppTheme
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatContent(
    chatState: ChatScreenState,
    handleAction: (ChatViewModel.ChatAction) -> Unit
) {
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Button(
                                onClick = {
                                    handleAction(ChatViewModel.ChatAction.OnBackClicked)
                                },
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(24.dp)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.shevron_left),
                                    contentDescription = stringResource(R.string.profile_shevron_icon_content_description),
                                )
                            }

                            if (chatState.topBarState?.isLoading == true) {
                                LoadingState(Modifier, color = Color.White)
                            } else {
                                AsyncImage(
                                    model = chatState.topBarState?.chatAvatarUrl,
                                    contentDescription = "xsscs",
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                )
                                Column(
                                    modifier = Modifier
                                        .padding(start = 12.dp, end = 4.dp)
                                        .weight(1f),
                                ) {

                                    Text(
                                        text = chatState.topBarState?.chatName.toString(),
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                    if (chatState.topBarState?.chatType != "d") {
                                        Text(
                                            text = "Участники: " + chatState.topBarState?.numberOfMembers.toString(),
                                            fontSize = 11.sp,
                                            maxLines = 1,
                                        )
                                    }
                                }
                            }

                            Button(
                                onClick = {

                                },
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier
                                    .padding(end = 16.dp)
                                    .size(24.dp)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.more),
                                    contentDescription = stringResource(R.string.profile_shevron_icon_content_description),
                                )
                            }

                        }
                    }
                }
            )
        },
        bottomBar = {
            ChatInputField(
                value = chatState.textField,
                onValueChange = { text ->
                    handleAction(ChatViewModel.ChatAction.OnMessageTextChanged(text))
                },
                onSendClick = {
                    handleAction(ChatViewModel.ChatAction.OnSendMessageClick(chatState.textField))
                },
                modifier = Modifier.imeNestedScroll()

            )
        }
    ) { paddingValues ->

        val chatListState = rememberLazyListState()
        LaunchedEffect(chatState.messages.size) {
            chatListState.animateScrollToItem(0)
        }

        Column(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            when {
                chatState.isLoading -> LoadingState()
                chatState.error != null -> ErrorState(Modifier, chatState.error)
                chatState.messages.isEmpty() -> EmptyState(
                    modifier = Modifier,
                    message = "Чат создан, сообщений нет"
                )

                else -> MessagesList(chatListState, paddingValues, chatState, handleAction)
            }
        }
    }
}


@Composable
private fun MessagesList(
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


@Composable
private fun ChatInputField(
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .shadow(elevation = 8.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .windowInsetsPadding(WindowInsets.ime),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .weight(1f),
            placeholder = { Text("Текст сообщения") },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            maxLines = 5
        )

        IconButton(
            onClick = onSendClick,
            enabled = value.isNotEmpty()
        ) {
            if (value.isNotEmpty()) {
                MessageIcon(
                    painter = painterResource(R.drawable.send),
                    contentDescription = "",
                )
            } else {

                Icon(
                    painter = painterResource(R.drawable.attach),
                    contentDescription = "Отправить",
                    tint =
                        MaterialTheme.colorScheme.secondary
                )
            }

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
                            text = chatState.messageType.fileName.toString(),
                            softWrap = true,
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

@Preview(showBackground = true)
@Composable
fun MessagesPreview() {
    AppTheme {
        ChatContent(
            chatState = ChatScreenState(
                topBarState = ChatScreenState.TopBarState(
                    chatName = "sdkkkkkkkkkkkkkkkkkkkkkkkkkkkx",
                    isLoading = false
                ),
                textField = "sfs"
            )
        ) { }
    }

}

@Preview(showBackground = true)
@Composable
fun MessageItemSystemPreview() {
    AppTheme {
        MessageItem(
            chatState = MessageState(
                id = "sx",
                isMeAuthor = true,
                messageAuthorName = "Olga",
                messageTimestamp = 0,
                messageType = MessageState.MessageType.System("Warning")
            )
        ) {

        }
    }
}


@Preview(showBackground = true)
@Composable
fun MessageItemTextPreview() {
    AppTheme {
        MessageItem(
            chatState = MessageState(
                id = "sx",
                isMeAuthor = true,
                messageAuthorName = "Olga",
                messageTimestamp = 0,
                messageType = MessageState.MessageType.Text("Hi")
            )
        ) {

        }
    }
}


@Preview(showBackground = true)
@Composable
fun MessageItemVideoPreview() {
    AppTheme {
        MessageItem(
            chatState = MessageState(
                id = "sx",
                isMeAuthor = true,
                messageAuthorName = "Olga",
                messageTimestamp = 0,
                messageType = MessageState.MessageType.Video(
                    text = null,
                    videoType = "ad",
                    videoName = "zzdzssssssssszxssssssssssssssxaxa.jar",
                    videoUrl = "z"
                )
            )
        ) {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun MessageItemFilePreview() {
    AppTheme {
        MessageItem(
            chatState = MessageState(
                id = "sx",
                isMeAuthor = true,
                messageAuthorName = "Olga",
                messageTimestamp = 0,
                messageType = MessageState.MessageType.File(
                    text = null,
                    fileName = "ca,dddddddddddddkdxd",
                    fileUrl = ""
                )
            )
        ) {

        }
    }
}
