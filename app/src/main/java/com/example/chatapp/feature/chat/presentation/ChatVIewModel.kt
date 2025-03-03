package com.example.chatapp.feature.chat.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.feature.chat.domain.CreateMessageUseCase
import com.example.chatapp.feature.chat.domain.GetRoomInfoUseCase
import com.example.chatapp.feature.chat.domain.ObserveMessagesUse
import com.example.chatapp.feature.chatList.presentation.RoomState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ChatViewModel.Factory::class)
class ChatViewModel @AssistedInject constructor(
    @Assisted private val roomId: String,
    @Assisted private val roomTypeState: RoomState.RoomTypeState,
    private val observeMessagesUse: ObserveMessagesUse,
    private val createMessageUseCase: CreateMessageUseCase,
    private val getRoomInfoUseCase: GetRoomInfoUseCase,
) : ViewModel() {

    private val mutableChatListState = MutableStateFlow<ChatScreenState>(ChatScreenState())
    val chatListState: StateFlow<ChatScreenState> = mutableChatListState.asStateFlow()

    private val mutableEvents = Channel<ChatEvent>()
    val events = mutableEvents.receiveAsFlow()

    init {
        mutableChatListState.value = chatListState.value.copy(
            isLoading = true,
            topBarState = ChatScreenState.TopBarState(isLoading = true)
        )

        viewModelScope.launch {
            observeMessagesUse(
                roomId = roomId,
                stateFlow = { stateFlow ->
                    viewModelScope.launch {
                        stateFlow.collect { updatedMessages ->
                            if (updatedMessages != null) {
                                val messagesList =
                                    updatedMessages.map { it.toMessageState() } ?: emptyList()
                                mutableChatListState.value = chatListState.value.copy(
                                    messages = messagesList,
                                    isLoading = false,
                                )
                            }
                        }
                    }
                }
            )
        }

        viewModelScope.launch {
            mutableChatListState.value = chatListState.value.copy(
                isLoading = true
            )
            runCatching {
                val room = getRoomInfoUseCase(roomId = roomId)
                mutableChatListState.value = chatListState.value.copy(
                    topBarState = room.topTopBarState(roomId)
                )
            }.onFailure {
                print(it)
            }
        }
    }

    fun handleAction(action: ChatAction) {
        when (action) {
            ChatAction.OnBackClicked -> onBackClicked()
            ChatAction.OnDeleteImageClick -> {
                deleteImage()
            }

            is ChatAction.OnMessageTextChanged -> {
                messageFieldChanged(action.text)
            }

            is ChatAction.OnSendMessageClick -> {
                sendMessage(text = action.text, uri = action.uri)
            }

            is ChatAction.OnAttachClick -> {
                openPicker()
            }

            is ChatAction.OnImageSelect -> {
                setImage(action.selected)
            }
        }
    }

    private fun onBackClicked() {
        viewModelScope.launch {
            mutableEvents.send(ChatEvent.NavigateBack)
        }
    }

    private fun sendMessage(text: String, uri: Uri?) {
        viewModelScope.launch {
            runCatching {
                createMessageUseCase(roomId = roomId, text = text)
                messageFieldChanged("")
            }.onFailure {
                print(it)
            }
        }

    }

    private fun messageFieldChanged(textField: String) {
        mutableChatListState.value = chatListState.value.copy(textField = textField)
    }

    private fun openPicker() {
        viewModelScope.launch {
            mutableEvents.send(ChatEvent.OpenImagePicker)
        }
    }

    private fun setImage(imageUri: Uri?) {
        mutableChatListState.value = chatListState.value.copy(selectedImages = listOf(imageUri))
    }

    private fun deleteImage() {
        mutableChatListState.value = chatListState.value.copy(selectedImages = null, textField = "")

    }


    sealed class ChatAction {
        data object OnBackClicked : ChatAction()
        data class OnMessageTextChanged(val text: String) : ChatAction()
        data class OnSendMessageClick(val text: String, val uri: Uri?) : ChatAction()
        data object OnAttachClick : ChatAction()
        data class OnImageSelect(val selected: Uri?) : ChatAction()
        data object OnDeleteImageClick : ChatAction()
    }

    sealed class ChatEvent {
        data object NavigateBack : ChatEvent()
        data object OpenImagePicker : ChatEvent()
    }

    @AssistedFactory
    interface Factory {
        fun create(roomId: String, roomTypeState: RoomState.RoomTypeState): ChatViewModel
    }
}

