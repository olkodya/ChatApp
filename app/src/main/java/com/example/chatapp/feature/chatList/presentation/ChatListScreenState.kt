package com.example.chatapp.feature.chatList.presentation

import androidx.compose.runtime.Immutable
import com.example.chatapp.components.ErrorState

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
    val imageUrl: String?,
    private val type: String,
    val name: String?,
    val lastMassage: String?,
    val lastMessageDate: String?,
    private val lastMessageAuthor: String?,
    private val isMeMessageAuthor: Boolean, // Don't work, not truth value
    private val unreadMessagesNumber: Int,
) {

    val showedName: String?
        get() = if (isMeMessageAuthor.not()) {
            lastMessageAuthor
        } else {
            null
        }


    val showedImageUrl: String
        get() {
            // name is null
            val i = if (type == "c") {
                "https://eltex2025.rocket.chat/api/v1/avatar/@$name"
            } else {
                "https://eltex2025.rocket.chat/api/v1/avatar/$name"
            }
            return i
        }

    val showedUnreadMessages: Int?
        get() = when {
            isMeMessageAuthor.not() -> unreadMessagesNumber
            else -> null
        }

    val showedLastMessageAuthor: String?
        get() = when {
            type == "c" -> {
                if (isMeMessageAuthor) "Вы"
                else lastMessageAuthor
            }

            else -> null
        }

    val numberOfCheckMark: Int?
        get() = when {
            (isMeMessageAuthor && unreadMessagesNumber > 0) -> 1
            (isMeMessageAuthor && unreadMessagesNumber == 0) -> 2
            else -> null
        }
}
