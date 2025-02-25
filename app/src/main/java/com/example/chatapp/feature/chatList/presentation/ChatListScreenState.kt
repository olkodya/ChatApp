package com.example.chatapp.feature.chatList.presentation

import androidx.compose.runtime.Immutable
import coil3.Image
import com.example.chatapp.components.ErrorState
import kotlinx.collections.immutable.ImmutableList

@Immutable
sealed class ChatListScreenState {

    @Immutable
    data class Content(
        val rooms: List<RoomState>
    ) : ChatListScreenState()

    @Immutable
    data object Loading : ChatListScreenState()

    @Immutable
    data class Error(val state: ErrorState) : ChatListScreenState()
}

@Immutable
data class RoomState(
    val id: String,
    val imageUrl: String,
    val type: String,
    val name: String,
    val lastMassage: String,
    val lastMessageDate: String,
)

