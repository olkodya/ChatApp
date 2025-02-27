package com.example.chatapp.feature.chatList.presentation

import androidx.compose.runtime.Immutable
import com.example.chatapp.components.ErrorState

sealed class UserListState {

    @Immutable
    data class Content(
        val users: List<UserState>
    ) : UserListState()

    @Immutable
    data object Loading : UserListState()

    @Immutable
    data class Error(val state: ErrorState) : UserListState()
}

data class UserState(
    val id: String,
    val avatarUrl: String,
    val name: String,
    val username: String,
)
//
//data class UserState(
//    a
//)