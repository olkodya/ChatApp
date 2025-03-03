package com.example.chatapp.feature.chat.presentation

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.chatapp.R
import com.example.chatapp.components.EmptyState
import com.example.chatapp.components.ErrorState
import com.example.chatapp.components.LoadingState
import com.example.chatapp.feature.chat.presentation.compnents.ChatTopAppBar
import com.example.chatapp.feature.chat.presentation.compnents.MessageIcon
import com.example.chatapp.feature.chat.presentation.compnents.MessageItem
import com.example.chatapp.feature.chat.presentation.compnents.MessagesList
import com.example.chatapp.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatContent(
    chatState: ChatScreenState,
    handleAction: (ChatViewModel.ChatAction) -> Unit,
    selectedImages: List<Uri?>?
) {
    WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    Scaffold(
        topBar = {
            ChatTopAppBar(chatState, handleAction)
        },
        bottomBar = {
            Column {
                if (selectedImages.isNullOrEmpty().not()) {
                    ImageLayoutView(
                        selectedImages = chatState.selectedImages,
                        modifier = Modifier.fillMaxWidth(),
                        handleAction = handleAction
                    )
                }
                ChatInputField(
                    value = chatState.textField,
                    onValueChange = { text ->
                        handleAction(ChatViewModel.ChatAction.OnMessageTextChanged(text))
                    },
                    onSendClick = {
                        handleAction(ChatViewModel.ChatAction.OnSendMessageClick(chatState.textField, chatState.selectedImages?.firstOrNull()))
                    },
                    modifier = Modifier.imeNestedScroll(),
                    selectedImages,
                    handleAction = handleAction
                )
            }

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

                else -> {
                    MessagesList(chatListState, paddingValues, chatState, handleAction)
                }
            }
        }
    }

}

@Composable
private fun ChatInputField(
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedImages: List<Uri?>?,
    handleAction: (ChatViewModel.ChatAction) -> Unit
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

        if (value.isNotEmpty() || selectedImages.isNullOrEmpty().not()) {
            IconButton(
                onClick = { onSendClick() },
            ) {
                MessageIcon(
                    painter = painterResource(R.drawable.send),
                    contentDescription = "",
                )
            }
        } else {
            IconButton(
                onClick = { handleAction(ChatViewModel.ChatAction.OnAttachClick) },
            ) {
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
            ),
            { },
            selectedImages = listOf()
        )
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

@Composable
fun ImageLayoutView(
    selectedImages: List<Uri?>?,
    modifier: Modifier = Modifier,
    handleAction: (ChatViewModel.ChatAction) -> Unit
) {
    if (selectedImages.isNullOrEmpty().not()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = selectedImages.firstOrNull(),
                    contentDescription = "Выбранное изображение",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Информация о файле
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Изображение",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = selectedImages.firstOrNull().toString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = { handleAction(ChatViewModel.ChatAction.OnDeleteImageClick) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Удалить",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
