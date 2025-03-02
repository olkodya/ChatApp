package com.example.chatapp.feature.chat.presentation

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    handleAction: (ChatViewModel.ChatAction) -> Unit
) {
    WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    Scaffold(
        topBar = {
            ChatTopAppBar(chatState, handleAction)
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
    modifier: Modifier = Modifier
) {

    var selectedImages by remember {
        mutableStateOf<List<Uri?>>(emptyList())
    }

    val maxSelectionCount = 1


    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImages = listOf(uri) }
    )


    fun launchPhotoPicker() {
        singlePhotoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    ImageLayoutView(selectedImages = selectedImages)

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
            onClick = { launchPhotoPicker() },
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

@Composable
fun ImageLayoutView(selectedImages: List<Uri?>) {
    Box() {
        AsyncImage(
            model = selectedImages.firstOrNull(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentScale = ContentScale.Fit
        )
    }
}
