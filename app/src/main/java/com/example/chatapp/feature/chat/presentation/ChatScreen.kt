package com.example.chatapp.feature.chat.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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



    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> viewModel.handleAction(ChatViewModel.ChatAction.OnImageSelect(listOf(uri).firstOrNull())) }
    )


    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                ChatViewModel.ChatEvent.NavigateBack -> navigateBack()
                ChatViewModel.ChatEvent.OpenImagePicker ->  singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        }
    }

    ChatContent(
        chatState = viewModelState,
        handleAction = viewModel::handleAction,
        selectedImages = viewModelState.selectedImages
    )
}
