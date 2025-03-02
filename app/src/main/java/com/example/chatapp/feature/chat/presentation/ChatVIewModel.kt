package com.example.chatapp.feature.chat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.feature.chat.domain.CreateMessageUseCase
import com.example.chatapp.feature.chat.domain.ObserveMessagesUse
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ChatViewModel.Factory::class)
class ChatViewModel @AssistedInject constructor(
    @Assisted private val roomId: String,
    private val observeMessagesUse: ObserveMessagesUse,
    private val createMessageUseCase: CreateMessageUseCase
) : ViewModel() {

    private val mutableChatListState = MutableStateFlow<ChatScreenState>(ChatScreenState())
    val chatListState: StateFlow<ChatScreenState> = mutableChatListState.asStateFlow()

    private val mutableEvents = Channel<ChatEvent>()
    val events = mutableEvents.receiveAsFlow()

    init {
        mutableChatListState.value = chatListState.value.copy(
            isLoading = true,
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
    }

    fun handleAction(action: ChatAction) {
        when (action) {
            ChatAction.OnBackClicked -> onBackClicked()
            is ChatAction.OnMessageTextChanged -> {
                messageFieldChanged(action.text)
            }

            is ChatAction.OnSendMessageClick -> {
                sendMessage(text = action.text)
            }
        }
    }

    private fun onBackClicked() {
        viewModelScope.launch {
            mutableEvents.send(ChatEvent.NavigateBack)
        }
    }

    private fun sendMessage(text: String) {
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

    sealed class ChatAction {
        data object OnBackClicked : ChatAction()
        data class OnMessageTextChanged(val text: String) : ChatAction()
        data class OnSendMessageClick(val text: String) : ChatAction()
    }

    sealed class ChatEvent {
        data object NavigateBack : ChatEvent()

    }

    @AssistedFactory
    interface Factory {
        fun create(roomId: String): ChatViewModel
    }

}

