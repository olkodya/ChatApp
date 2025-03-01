package com.example.chatapp.feature.chat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlin.collections.map

@HiltViewModel(assistedFactory = ChatViewModel.Factory::class)
class ChatViewModel @AssistedInject constructor(
    @Assisted roomId: String,
    private val observeMessagesUse: ObserveMessagesUse,
) : ViewModel() {

    private val mutableChatListState = MutableStateFlow<ChatScreenState>(ChatScreenState())
    val chatListState: StateFlow<ChatScreenState> = mutableChatListState.asStateFlow()

    private val mutableEvents = Channel<ChatEvent>()
    val events = mutableEvents.receiveAsFlow()

    init {
        viewModelScope.launch {
            observeMessagesUse(
                roomId = roomId,
                stateFlow = { stateFlow ->
                    viewModelScope.launch {
                        stateFlow.collectLatest { updatedMessages ->
                            if (updatedMessages != null) {
                                val messagesList =
                                    updatedMessages.map { it.toMessageState() } ?: emptyList()
                                mutableChatListState.value = chatListState.value.copy(
                                    messages = messagesList
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
        }
    }

    private fun onBackClicked() {
        viewModelScope.launch {
            mutableEvents.send(ChatEvent.NavigateBack)
        }
    }

    sealed class ChatAction {
        data object OnBackClicked : ChatAction()
    }

    sealed class ChatEvent {
        data object NavigateBack : ChatEvent()
    }

    @AssistedFactory
    interface Factory {
        fun create(roomId: String): ChatViewModel
    }
}
