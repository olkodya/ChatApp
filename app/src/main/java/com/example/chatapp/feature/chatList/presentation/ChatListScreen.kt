package com.example.chatapp.feature.chatList.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ChatListScreen() {
    val viewModel: ChatListViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ChatListViewModel.ChatListEvent.NavigateToChat -> TODO()
            }
        }
    }

    ChatListContent(
        fieldState = viewModel.fieldState.collectAsState().value,
        chatListState = viewModel.chatListState.collectAsState().value,
        handleAction = { viewModel.handleAction(it) }
    )
}




