package com.example.chatapp.feature.chat.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.chatapp.feature.chatList.presentation.RoomState

@Composable
fun ChatScreen(
    roomId: String,
    navigateBack: () -> Unit,
    roomType: RoomState.RoomTypeState,
) {

    val viewModel = hiltViewModel<ChatViewModel, ChatViewModel.Factory> { factory ->
        factory.create(roomId, roomType)
    }
    val viewModelState: ChatScreenState by viewModel.chatListState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                ChatViewModel.ChatEvent.NavigateBack -> navigateBack()
            }
        }
    }

    ChatContent(
        chatState = viewModelState,
        handleAction = viewModel::handleAction
    )
}
