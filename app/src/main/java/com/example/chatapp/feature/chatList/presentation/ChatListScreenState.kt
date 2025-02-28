package com.example.chatapp.feature.chatList.presentation

import androidx.compose.runtime.Immutable
import com.example.chatapp.components.ErrorState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class ChatListScreenState(
    val errorState: ErrorState? = null,
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val rooms: ImmutableList<RoomState> = persistentListOf(),
) {
    val isSuccessLoaded: Boolean
        get() = isLoading == false && errorState == null && rooms.isNotEmpty()
}

@Immutable
data class RoomState(
    val id: String,
    val imageUrl: String?,
    val type: RoomType,
    val name: String?,
    val lastMassage: String?,
    val lastUpdateTimestamp: Long?,
    val lastMessageAuthor: String?,
    val isMeMessageAuthor: Boolean,
    val isLastMessageExist: Boolean,
    val unreadMessagesCount: Int?,
    val userName: String?,
    val numberOfCheckMark: Int?,
    val lastMessageType: LastMessageType?
) {

    @Immutable
    enum class RoomType {
        DIRECT,                 // Direct messages
        PUBLIC_CHANNEL,         // Public channel
        PRIVATE_CHANNEL,        // Private channel
        DISCUSSIONS,            // Team or channel discussions
        TEAMS,                  // Workspace teams
        LIVECHAT,               // Livechat
        VOIP,                   // Omnichannel VoIP rooms
        UNKNOWN;
    }

    @Immutable
    enum class LastMessageType {
        TEXT,
        VIDEO,
        IMAGE,
        DOCUMENT,
        UNKNOWN;
    }
}
