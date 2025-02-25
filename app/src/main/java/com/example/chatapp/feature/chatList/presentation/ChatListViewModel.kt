package com.example.chatapp.feature.chatList.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.feature.chatList.domain.ObserveRoomsUseCase
import com.example.chatapp.feature.chatList.domain.model.RoomEntity
import com.example.chatapp.feature.chatList.domain.model.toRoomState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val observeRoomsUseCase: ObserveRoomsUseCase,
) : ViewModel() {
    private val mutableFieldState: MutableStateFlow<String> = MutableStateFlow("")
    val fieldState: StateFlow<String> = mutableFieldState.asStateFlow()

    private val mutableChatListState =
        MutableStateFlow<ChatListScreenState>(ChatListScreenState.Loading)
    val chatListState: StateFlow<ChatListScreenState> = mutableChatListState.asStateFlow()

    private val mutableEvents = Channel<ChatListEvent>()
    val events = mutableEvents.receiveAsFlow()

    init {
        loadChats()
    }

    fun handleAction(action: ChatListAction) {
        when (action) {
            ChatListAction.OnAddChatClicked -> Unit
            is ChatListAction.OnChatClicked -> Unit
            is ChatListAction.OnSearchFieldEdited -> fieldChanged(action.query)
        }
    }

    private fun loadChats() {
        mutableChatListState.value = ChatListScreenState.Loading
        viewModelScope.launch {
            runCatching {
                val roomsEntities: StateFlow<List<RoomEntity>?> = observeRoomsUseCase()
                roomsEntities.collectLatest { updatedRooms ->
                    if (roomsEntities.value == null) {
                        mutableChatListState.value = ChatListScreenState.Loading

                    } else
                        mutableChatListState.value =
                            ChatListScreenState.Content(rooms = updatedRooms?.map { it.toRoomState() }
                                ?: emptyList())
                }
            }.onFailure {
                print(it)
                mutableChatListState.value = ChatListScreenState.Loading
            }
        }
    }

    private fun fieldChanged(query: String) {
        mutableFieldState.value = query
    }

    sealed class ChatListAction {
        data class OnSearchFieldEdited(val query: String) : ChatListAction()
        data class OnChatClicked(val chatId: String) : ChatListAction()
        data object OnAddChatClicked : ChatListAction()
    }

    sealed class ChatListEvent {
        data object NavigateToChat : ChatListEvent()
    }
}