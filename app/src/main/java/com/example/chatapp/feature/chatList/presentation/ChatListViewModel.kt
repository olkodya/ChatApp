package com.example.chatapp.feature.chatList.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.feature.chatList.domain.CreateChatUseCase
import com.example.chatapp.feature.chatList.domain.GetUsersUseCase
import com.example.chatapp.feature.chatList.domain.ObserveRoomsUseCase
import com.example.chatapp.feature.chatList.domain.model.RoomEntity
import com.example.chatapp.feature.chatList.domain.model.UserEntity
import com.example.chatapp.feature.chatList.domain.model.toRoomState
import com.example.chatapp.feature.chatList.domain.model.toUserState
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
    private val getUsersUseCase: GetUsersUseCase,
    private val createChatUseCase: CreateChatUseCase
) : ViewModel() {
    private val mutableChatsFieldState: MutableStateFlow<String> = MutableStateFlow("")
    val chatsFieldState: StateFlow<String> = mutableChatsFieldState.asStateFlow()

    private val mutableUsersFieldState: MutableStateFlow<String> = MutableStateFlow("")
    val usersFieldState: StateFlow<String> = mutableUsersFieldState.asStateFlow()

    private val mutableChatListState =
        MutableStateFlow<ChatListScreenState>(ChatListScreenState.Loading)
    val chatListState: StateFlow<ChatListScreenState> = mutableChatListState.asStateFlow()

    private val mutableUserListState =
        MutableStateFlow<UserListState>(UserListState.Loading)
    val userListState: StateFlow<UserListState> = mutableUserListState.asStateFlow()

    private val mutableEvents = Channel<ChatListEvent>()
    val events = mutableEvents.receiveAsFlow()

    init {
        loadChats()
    }

    fun handleAction(action: ChatListAction) {
        when (action) {
            ChatListAction.OnAddChatClicked -> getUsers()
            is ChatListAction.OnChatClicked -> Unit
            is ChatListAction.OnSearchChatsFieldEdited -> chatsFieldChanged(action.query)
            is ChatListAction.OnSearchUsersFieldEdited -> usersFieldChanged(action.query)
            is ChatListAction.OnUserClicked -> createChat(username = action.username)
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

    private fun createChat(username: String) {
        viewModelScope.launch {
            runCatching {
                createChatUseCase(username = username)

            }.onFailure {
                print(it.message)
            }
        }
    }

    private fun getUsers() {
        viewModelScope.launch {
            runCatching {
                val usersEntities: List<UserEntity> = getUsersUseCase()
                mutableUserListState.value =
                    UserListState.Content(users = usersEntities.map { it.toUserState() })
            }.onFailure {
                print(it.message)
            }
            mutableEvents.send(ChatListEvent.ShowBottomSheet)

        }
    }

    private fun chatsFieldChanged(query: String) {
        mutableChatsFieldState.value = query
    }

    private fun usersFieldChanged(query: String) {
        mutableUsersFieldState.value = query
    }

    sealed class ChatListAction {
        data class OnSearchChatsFieldEdited(val query: String) : ChatListAction()
        data class OnSearchUsersFieldEdited(val query: String) : ChatListAction()
        data class OnChatClicked(val chatId: String) : ChatListAction()
        data object OnAddChatClicked : ChatListAction()
        data class OnUserClicked(val username: String) : ChatListAction()
    }

    sealed class ChatListEvent {
        data object NavigateToChat : ChatListEvent()
        data object ShowBottomSheet : ChatListEvent()
    }
}