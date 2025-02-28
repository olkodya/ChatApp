package com.example.chatapp.feature.chatList.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
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
import com.example.chatapp.components.LoadingState
import com.example.chatapp.components.SearchTextField
import com.example.chatapp.components.Shimmer
import com.example.chatapp.feature.chatList.presentation.RoomState.LastMessageType
import com.example.chatapp.feature.chatList.presentation.RoomState.RoomType
import com.example.chatapp.ui.theme.AppTheme
import kotlinx.collections.immutable.persistentListOf
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListContent(
    chatListState: ChatListScreenState,
    handleAction: (ChatListViewModel.ChatListAction) -> Unit
) {
    val screenHeight: Dp = LocalConfiguration.current.screenHeightDp.dp
    val headerHeight: Dp = screenHeight / 5

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight)
                .background(color = MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.size(headerHeight / 3))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = stringResource(R.string.chat_list_title), modifier = Modifier
                            .weight(1f)
                            .wrapContentWidth(),
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp
                    )
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Button(
                            onClick = {
                                handleAction(ChatListViewModel.ChatListAction.OnAddChatClicked)
                            },
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier
                                .size(24.dp)
                                .wrapContentWidth(),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = stringResource(R.string.chat_list_add_icon_content_description),
                            )
                        }
                    }
                }
                Spacer(Modifier.weight(1f))
                SearchTextField(
                    value = chatListState.searchQuery,
                    isEnabled = chatListState.isSuccessLoaded,
                    onValueChange = { value ->
                        handleAction(ChatListViewModel.ChatListAction.OnSearchChatsFieldEdited(value))
                    },
                    placeholder = stringResource(R.string.chat_list_search_plaseholder),
                    paddingValues = PaddingValues(bottom = 8.dp)
                )
            }
        }

        if (chatListState.isLoading) {
            LoadingState()
        } else if (chatListState.errorState != null) {
        } else {
            ChatList(state = chatListState, handleAction = handleAction)
        }
    }
}

@Composable
private fun ChatList(
    state: ChatListScreenState,
    handleAction: (ChatListViewModel.ChatListAction) -> Unit
) {

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
    ) {
        items(
            count = state.rooms.size,
            key = { id -> state.rooms[id].id },
        ) { index ->
            val chat = state.rooms[index]
            ChatItem(chatState = chat, handleAction)
        }
    }
}

@Composable
private fun ChatItem(
    chatState: RoomState,
    handleAction: (ChatListViewModel.ChatListAction) -> Unit,
) {
    val bordersColor = MaterialTheme.colorScheme.surfaceVariant
    Row(
        modifier = Modifier
            .height(72.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 0.dp)
            .clickable { handleAction(ChatListViewModel.ChatListAction.OnChatClicked(chatState.id)) }
            .drawBehind {
                drawLine(
                    color = bordersColor,
                    start = Offset(52.dp.toPx(), 0.dp.toPx()),
                    end = Offset(size.width + 16.dp.toPx(), 0.dp.toPx()),
                    strokeWidth = 1.dp.toPx()
                )
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = chatState.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            val showedName = chatState.name
            if (showedName != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = showedName,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    MessageDoneMark(chatState)
                    if (chatState.lastMessageType != LastMessageType.UNKNOWN) {
                        chatState.lastUpdateTimestamp?.let { lastUpdateTimestamp ->
                            LastRoomUpdateDate(timestamp = lastUpdateTimestamp)
                        }
                    }
                }
            } else {
                Shimmer(
                    modifier = Modifier
                        .height(20.dp)
                        .width(150.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LastMessageAuthor(chatState)
                LastMessageText(chatState, Modifier.weight(1f))
                chatState.unreadMessagesCount?.let { unreadMessagesCount ->
                    MessageCounter(unreadMessagesCount)
                }
            }
        }
    }
}

@Composable
private fun LastMessageAuthor(state: RoomState) {
    val showedLastMessageAuthor: String? = when {
        state.type == RoomType.PUBLIC_CHANNEL -> {
            if (state.isMeMessageAuthor) stringResource(R.string.chat_list_you)
            else if (state.lastMessageAuthor == null) ""
            else "${state.lastMessageAuthor}: "
        }

        else -> null
    }
    showedLastMessageAuthor?.let { author ->
        Text(
            text = author,
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = 14.sp,
            maxLines = 1,
        )
    }
}

@Composable
private fun LastMessageText(state: RoomState, modifier: Modifier) {
    val lastMessageText: String? = when (state.lastMessageType) {
        LastMessageType.IMAGE -> {
            "Изображение"
        }

        LastMessageType.VIDEO -> {
            "Видео"
        }

        LastMessageType.DOCUMENT -> {
            "Документ"
        }

        LastMessageType.TEXT -> {
            state.lastMassage
        }

        LastMessageType.UNKNOWN -> {
            "Сообщений нет"
        }

        else -> null
    }
    lastMessageText?.let {
        Text(
            text = lastMessageText,
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier
        )
    }
}


@Composable
private fun LastRoomUpdateDate(timestamp: Long) {

    val messageDateTime = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(timestamp),
        ZoneId.systemDefault()
    )
    val now = LocalDateTime.now()

    val showedLastMessageDate: String = when {
        messageDateTime.toLocalDate() == now.toLocalDate() -> {
            messageDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        }

        ChronoUnit.DAYS.between(
            messageDateTime.toLocalDate(),
            now.toLocalDate()
        ) < 7 -> {
            when (messageDateTime.format(DateTimeFormatter.ofPattern("EE"))
                .lowercase()) {
                "mon" -> stringResource(R.string.chat_list_weekday_monday)
                "tue" -> stringResource(R.string.chat_list_weekday_tuesday)
                "wed" -> stringResource(R.string.chat_list_weekday_wednesday)
                "thu" -> stringResource(R.string.chat_list_weekday_thursday)
                "fri" -> stringResource(R.string.chat_list_weekday_friday)
                "sat" -> stringResource(R.string.chat_list_weekday_saturday)
                "sun" -> stringResource(R.string.chat_list_weekday_sunday)
                else -> messageDateTime.format(DateTimeFormatter.ofPattern("EE"))
            }
                .lowercase()
                .replaceFirstChar { it.uppercase() }
        }

        else -> {
            messageDateTime.format(DateTimeFormatter.ofPattern("dd.MM"))
        }
    }

    Text(
        text = showedLastMessageDate,
        color = MaterialTheme.colorScheme.tertiary,
        fontSize = 12.sp
    )
}

@Composable
fun MessageCounter(unreadMessagesCount: Int) {
    Box(
        Modifier
            .padding(start = 4.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondary)
            .defaultMinSize(minWidth = 16.dp, minHeight = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 4.dp),
            text = unreadMessagesCount.toString(),
            textAlign = TextAlign.Center,
            style = TextStyle(lineHeight = 13.sp),
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}


@Composable
fun MessageDoneMark(chatState: RoomState) {
    Log.d("authir", chatState.numberOfCheckMark.toString())
    if (chatState.numberOfCheckMark == 1)
        Icon(
            painter = painterResource(R.drawable.unread_24),
            contentDescription = stringResource(R.string.chat_list_last_message_read_content_descriptiom),
            Modifier
                .padding(end = 4.dp)
                .size(12.dp),
            tint = MaterialTheme.colorScheme.secondary,

            ) else if (chatState.numberOfCheckMark == 2) {
        Icon(
            painter = painterResource(R.drawable.read_24),
            contentDescription = stringResource(R.string.chat_list_last_message_unread_content_descriptiom),
            Modifier
                .padding(end = 4.dp)
                .size(12.dp),
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChatCardPreview() {
    AppTheme {
        ChatItem(
            RoomState(
                id = "1",
                userName = "",
                imageUrl = "",
                type = RoomType.PUBLIC_CHANNEL,
                name = "ssd",
                lastMassage = "Коммутаторы коммутируют",
                lastUpdateTimestamp = 0,
                lastMessageAuthor = "xsxs",
                isMeMessageAuthor = false,
                unreadMessagesCount = 1,
                numberOfCheckMark = 1,
                isLastMessageExist = true,
                lastMessageType = LastMessageType.TEXT
            )
        ) { }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatCardPreviewAuthorIsMe() {
    AppTheme {
        ChatItem(
            chatState = RoomState(
                id = "1",
                userName = "",
                imageUrl = "",
                type = RoomType.DIRECT,
                name = "ssd",
                lastMassage = "Коммутаторы коммутируютdd",
                lastUpdateTimestamp = 0,
                lastMessageAuthor = "xsxs",
                isMeMessageAuthor = true,
                unreadMessagesCount = 1,
                numberOfCheckMark = 1,
                isLastMessageExist = true,
                lastMessageType = LastMessageType.TEXT
            ),
            handleAction = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChatListPreview() {
    AppTheme {
        ChatListContent(
            chatListState = ChatListScreenState(
                rooms = persistentListOf(
                    RoomState(
                        id = "1",
                        userName = "",
                        imageUrl = "",
                        type = RoomType.DIRECT,
                        name = "Ольга Кукарцева",
                        lastMassage = "Коммутаторы коммутируют",
                        lastUpdateTimestamp = 0,
                        lastMessageAuthor = "xsxs",
                        isMeMessageAuthor = false,
                        unreadMessagesCount = 1,
                        numberOfCheckMark = 1,
                        isLastMessageExist = true,
                        lastMessageType = LastMessageType.TEXT
                    )
                )
            )
        ) {
        }
    }
}