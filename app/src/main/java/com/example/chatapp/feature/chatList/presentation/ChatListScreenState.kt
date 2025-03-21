package com.example.chatapp.feature.chatList.presentation

import androidx.compose.runtime.Immutable
import com.example.chatapp.components.ErrorState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable

@Immutable
data class ChatListScreenState(
    val errorState: ErrorState? = null,
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    private val rooms: ImmutableList<RoomState> = persistentListOf(),
) {
    val isSuccessLoaded: Boolean
        get() = isLoading == false && errorState == null && rooms.isNotEmpty()


    val filteredRoomsByQuery: ImmutableList<RoomState>
        get() = rooms.filter { room ->
            val nameLowerCase: String = (room.name ?: "").lowercase()
            val searchQueryLowerCase: String = searchQuery.lowercase()
            nameLowerCase.contains(searchQueryLowerCase)
        }.toImmutableList()
}

@Immutable
data class RoomState(
    val id: String,
    val imageUrl: String?,
    val type: RoomTypeState,
    val name: String?,
    val lastMassage: String?,
    val lastUpdateTimestamp: Long?,
    val lastMessageAuthor: String?,
    val isMeMessageAuthor: Boolean,
    val isLastMessageExist: Boolean,
    val unreadMessagesCount: Int?,
    val userName: String?,
    val numberOfCheckMark: Int?,
    val lastMessageType: LastMessageType
) {


    @Serializable
    @Immutable
    enum class RoomTypeState {
        DIRECT,
        PUBLIC_CHANNEL,
        PRIVATE_CHANNEL,
        DISCUSSIONS,
        TEAMS,
        LIVECHAT,
        VOIP,
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
