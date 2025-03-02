package com.example.chatapp.feature.chatCreation.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateChatBottomSheet(
    onDismiss: () -> Unit,
    sheetState: SheetState,
    onNavigateToChat: (chatId: String) -> Unit,
) {
    val bottomSheetViewModel: CreateChatViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        bottomSheetViewModel.events.collect { event ->
            when (event) {
                is CreateChatViewModel.CreateChatEvent.HideBottomSheet -> {
                    if (event.roomId == null) {
                        onDismiss()
                    } else {
                        onNavigateToChat(event.roomId)
                    }
                }
            }
        }
    }

    CreateChatBottomSheetContent(
        userListState = bottomSheetViewModel.userListState.collectAsState().value,
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        handleAction = { bottomSheetViewModel.handleAction(it) },
    )
}
