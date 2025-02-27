package com.example.chatapp.feature.chatCreation.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.components.toErrorState
import com.example.chatapp.feature.chatCreation.domain.CreateChatUseCase
import com.example.chatapp.feature.chatCreation.domain.GetUsersUseCase
import com.example.chatapp.feature.chatCreation.domain.model.toUserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateChatViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase, private val createChatUseCase: CreateChatUseCase
) : ViewModel() {

    private val mutableUserListState = MutableStateFlow<CreateChatState>(CreateChatState())
    val userListState: StateFlow<CreateChatState> = mutableUserListState.asStateFlow()

    private val mutableEvents = Channel<CreateChatEvent>()
    val events = mutableEvents.receiveAsFlow()

    init {
        getUsers()
    }

    fun handleAction(action: CreateChatAction) {
        when (action) {
            is CreateChatAction.OnUserClicked -> createChat(username = action.username)
            CreateChatAction.OnCancelButtonClick -> {
                hideBottomSheet()
            }

            is CreateChatAction.OnSearchUsersFieldEdited -> usersFieldChanged(action.query)
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
                mutableUserListState.value = userListState.value.copy(
                    isLoading = true,
                    errorState = null,
                )
                val usersList: List<UserState> = getUsersUseCase().map { it.toUserState() }
                mutableUserListState.value = userListState.value.copy(
                    users = usersList.toImmutableList(),
                    isLoading = false,
                    errorState = null
                )
            }.onFailure {
                mutableUserListState.value = userListState.value.copy(
                    errorState = it.toErrorState { getUsers() },
                    isLoading = false
                )
            }
        }
    }

    private fun usersFieldChanged(query: String) {
        mutableUserListState.value = userListState.value.copy(searchQuery = query)
    }

    fun hideBottomSheet() {
        viewModelScope.launch {
            mutableEvents.send(CreateChatEvent.HideBottomSheet)
        }
    }

    sealed class CreateChatAction {
        data object OnCancelButtonClick : CreateChatAction()
        data class OnUserClicked(val username: String) : CreateChatAction()
        data class OnSearchUsersFieldEdited(val query: String) : CreateChatAction()
    }

    sealed class CreateChatEvent {
        data object HideBottomSheet : CreateChatEvent()
    }
}