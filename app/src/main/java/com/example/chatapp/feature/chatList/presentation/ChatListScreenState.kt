package com.example.chatapp.feature.chatList.presentation

import androidx.compose.runtime.Immutable
import com.example.chatapp.components.ErrorState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


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
    val unreadMessagesCount: Int?,
    val userName: String?,
    val numberOfCheckMark: Int?,
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

    val showedLastMessageAuthor: String?
        get() = when {
            type == RoomType.PUBLIC_CHANNEL -> {
                if (isMeMessageAuthor) "Вы: "
                else if (lastMessageAuthor == null) ""
                else "${lastMessageAuthor}: "
            }
            else -> null
        }
}
