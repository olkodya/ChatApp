package com.example.chatapp.feature.chatList.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapp.feature.chatCreation.presentation.CreateChatBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    onNavigateToChat: () -> Unit
) {

    val viewModel: ChatListViewModel = hiltViewModel()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ChatListViewModel.ChatListEvent.NavigateToChat -> {
                    onNavigateToChat()
                }

                ChatListViewModel.ChatListEvent.ShowBottomSheet -> {
                    showBottomSheet = true
                }

                ChatListViewModel.ChatListEvent.HideBottomSheet -> {
                    showBottomSheet = false
                }
            }
        }
    }

    ChatListContent(
        chatListState = viewModel.chatListState.collectAsState().value,
        handleAction = { viewModel.handleAction(it) }
    )

    if (showBottomSheet) {
        CreateChatBottomSheet(
            onDismiss = {
                viewModel.handleAction(ChatListViewModel.ChatListAction.OnCancelButtonClick)
            },
            sheetState = sheetState,
        )
    }
}
