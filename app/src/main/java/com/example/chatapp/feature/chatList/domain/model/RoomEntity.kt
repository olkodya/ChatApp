package com.example.chatapp.feature.chatList.domain.model

import com.example.chatapp.feature.chatList.data.model.RoomResponse
import com.example.chatapp.feature.chatList.domain.model.RoomEntity.RoomType
import com.example.chatapp.feature.chatList.presentation.RoomState

data class RoomEntity(
    val id: String,
    val type: RoomType,
    val userName: String?,
    val name: String?,
    val userId: String?,
    val lastMessageContent: String?,
    val lastMessageAuthor: String?,
    val lastMessageAuthorId: String,
    val lastUpdateTimestamp: Long?,
    val isMeMessageAuthor: Boolean,
    val unreadMessagesNumber: Int,
) {

    enum class RoomType(val value: String) {
        DIRECT("d"),                // Direct messages
        PUBLIC_CHANNEL("c"),        // Public channel
        PRIVATE_CHANNEL("p"),       // Private channel
        DISCUSSIONS("discussions"), // Team or channel discussions
        TEAMS("teams"),             // Workspace teams
        LIVECHAT("l"),              // Livechat
        VOIP("v"),                  // Omnichannel VoIP rooms
        UNKNOWN("unknown");

        companion object {
            fun fromString(value: String): RoomType =
                RoomType.entries.find { it.value == value } ?: UNKNOWN
        }
    }
}

fun RoomResponse.toEntity(unreadMessagesNumber: Int, userId: String): RoomEntity {
    return RoomEntity(
        id = id,
        name = name,
        userName = null,
        userId = uids?.firstOrNull(),
        type = RoomType.fromString(type),
        lastMessageContent = if (msgs == 0) "Сообщений нет" else lastMessage?.message?.firstOrNull()?.value?.firstOrNull()?.value,
        lastMessageAuthor = lastMessage?.author?.name,
        lastMessageAuthorId = lastMessage?.author?.id ?: "",

        lastUpdateTimestamp = lastMessage?.updatedAt?.date,
//        lastMessageDate = if (msgs == 0) ts else lastMessage?.updatedAt?.date,

        unreadMessagesNumber = unreadMessagesNumber,
        isMeMessageAuthor = lastMessage != null && userId == lastMessage.author.id
    )
}

fun RoomEntity.toRoomState() = RoomState(
    id = id,
    imageUrl = if (type == RoomEntity.RoomType.PUBLIC_CHANNEL) {
        "https://eltex2025.rocket.chat/avatar/room/$id"
    } else {
        "https://eltex2025.rocket.chat/avatar/$userName"
    },
    type = type.toState(),
    name = name,
    userName = userName,
    lastMassage = lastMessageContent,
    lastUpdateTimestamp = lastUpdateTimestamp,
    lastMessageAuthor = lastMessageAuthor,
    isMeMessageAuthor = isMeMessageAuthor,
    unreadMessagesCount = when {
        isMeMessageAuthor.not() && unreadMessagesNumber != 0 -> unreadMessagesNumber
        else -> null
    },
    numberOfCheckMark = when {
        (isMeMessageAuthor && unreadMessagesNumber > 0) -> 1
        (isMeMessageAuthor && unreadMessagesNumber == 0) -> 2
        else -> null
    },
)

fun RoomEntity.RoomType.toState(): RoomState.RoomType = when (this) {
    RoomEntity.RoomType.DIRECT -> RoomState.RoomType.DIRECT
    RoomEntity.RoomType.PUBLIC_CHANNEL -> RoomState.RoomType.PUBLIC_CHANNEL
    RoomEntity.RoomType.PRIVATE_CHANNEL -> RoomState.RoomType.PRIVATE_CHANNEL
    RoomEntity.RoomType.DISCUSSIONS -> RoomState.RoomType.DISCUSSIONS
    RoomEntity.RoomType.TEAMS -> RoomState.RoomType.TEAMS
    RoomEntity.RoomType.LIVECHAT -> RoomState.RoomType.LIVECHAT
    RoomEntity.RoomType.VOIP -> RoomState.RoomType.VOIP
    RoomEntity.RoomType.UNKNOWN -> RoomState.RoomType.UNKNOWN
}
