package com.example.chatapp.feature.chatList.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.feature.chatList.domain.ObserveRoomsUseCase
import com.example.chatapp.feature.chatList.domain.model.toRoomState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
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

    private val mutableChatListState = MutableStateFlow<ChatListScreenState>(ChatListScreenState())
    val chatListState: StateFlow<ChatListScreenState> = mutableChatListState.asStateFlow()

    private val mutableEvents = Channel<ChatListEvent>()
    val events = mutableEvents.receiveAsFlow()

    init {
        loadChats()
    }

    fun handleAction(action: ChatListAction) {
        when (action) {
            is ChatListAction.OnChatClicked -> navigateToChat(
                id = action.chatId,
                roomTypeState = action.roomTypeState
            )

            is ChatListAction.OnSearchChatsFieldEdited -> chatsFieldChanged(action.query)
            ChatListAction.OnCancelButtonClick -> {
                hideBottomSheet()
            }

            ChatListAction.OnAddChatClicked -> showBottomSheet()
            is ChatListAction.OnUserClick -> navigateToChat(
                id = action.roomId,
                roomTypeState = RoomState.RoomTypeState.DIRECT
            )
        }
    }

    private fun loadChats() {
        mutableChatListState.value = chatListState.value.copy(errorState = null, isLoading = true)
        viewModelScope.launch {
            runCatching {
                observeRoomsUseCase(
                    stateFlow = { stateFlow ->
                        viewModelScope.launch {
                            stateFlow.collectLatest { updatedRooms ->
                                if (updatedRooms == null) {
                                    mutableChatListState.value =
                                        chatListState.value.copy(
                                            errorState = null,
                                            isLoading = true
                                        )
                                } else {
                                    val roomsList = updatedRooms
                                        .sortedByDescending { it.lastUpdateTimestamp }
                                        .map { it.toRoomState() }
                                    mutableChatListState.value = chatListState.value.copy(
                                        errorState = null,
                                        isLoading = false,
                                        rooms = roomsList.toImmutableList()
                                    )
                                }
                            }
                        }
                    }
                )
            }.onFailure {
                print(it)
            }
        }
    }

    private fun navigateToChat(id: String, roomTypeState: RoomState.RoomTypeState) {
        viewModelScope.launch {
            mutableEvents.send(ChatListEvent.NavigateToChat(chatId = id, roomType = roomTypeState))
        }

    }

    private fun showBottomSheet() {
        viewModelScope.launch {
            mutableEvents.send(ChatListEvent.ShowBottomSheet)
        }
    }

    private fun chatsFieldChanged(query: String) {
        mutableChatListState.value = chatListState.value.copy(searchQuery = query)
    }


    fun hideBottomSheet() {
        viewModelScope.launch {
            mutableEvents.send(ChatListEvent.HideBottomSheet)
        }
    }

    sealed class ChatListAction {
        data class OnSearchChatsFieldEdited(val query: String) : ChatListAction()
        data class OnChatClicked(val chatId: String, val roomTypeState: RoomState.RoomTypeState) :
            ChatListAction()

        data object OnAddChatClicked : ChatListAction()
        data object OnCancelButtonClick : ChatListAction()
        data class OnUserClick(val roomId: String) : ChatListAction()
    }

    sealed class ChatListEvent {
        data class NavigateToChat(val chatId: String, val roomType: RoomState.RoomTypeState) :
            ChatListEvent()

        data object ShowBottomSheet : ChatListEvent()
        data object HideBottomSheet : ChatListEvent()
    }
}
