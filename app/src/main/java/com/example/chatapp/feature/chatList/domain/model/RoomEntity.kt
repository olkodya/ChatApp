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
    val lastMessageType: LastMessageType,
    val isLastMessageExist: Boolean,
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

    enum class LastMessageType(val value: String) {
        TEXT("text"),
        IMAGE("image"),
        VIDEO("video"),
        DOCUMENT("document"),
        UNKNOWN("unknown");

        companion object {
//            fun fromString(value: String): LastMessageType {
//
//            }
        }

    }
}

fun RoomResponse.toEntity(
    unreadMessagesNumber: Int,
    userId: String,
    userNameMe: String
): RoomEntity {
    val lastMessageType = if (lastMessage?.message != null) {
        RoomEntity.LastMessageType.TEXT
    } else {
        if (lastMessage?.file != null) {
            if (lastMessage.file.type.contains("video")) {
                RoomEntity.LastMessageType.VIDEO
            } else if (lastMessage.file.type.contains("image")) {
                RoomEntity.LastMessageType.IMAGE
            } else {
                RoomEntity.LastMessageType.DOCUMENT
            }
        } else {
            RoomEntity.LastMessageType.UNKNOWN
        }
    }

    val userName = usernames?.size?.let {
        if (it > 1) {
            if (usernames[0] != userNameMe)
                usernames[0]
            else {
                usernames[1]
            }
        } else {
            userNameMe
        }
    }

    return RoomEntity(
        id = id,
        name = name,
        userName = userName,
        userId = uids?.firstOrNull(),
        type = RoomType.fromString(type),
        lastMessageContent = if (msgs == 0) "Сообщений нет" else lastMessage?.message?.firstOrNull()?.value?.firstOrNull()?.value,
        lastMessageAuthor = lastMessage?.author?.name,
        lastMessageAuthorId = lastMessage?.author?.id ?: "",
        lastUpdateTimestamp = if (msgs == 0) {
            ts?.date
        } else {
            lastMessage?.updatedAt?.date
        },
        unreadMessagesNumber = unreadMessagesNumber,
        isMeMessageAuthor = lastMessage != null && userId == lastMessage.author.id,
        isLastMessageExist = msgs != 0,
        lastMessageType = lastMessageType
    )
}

fun RoomEntity.toRoomState() = RoomState(
    id = id,
    imageUrl = if (type == RoomType.PUBLIC_CHANNEL) {
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
    isLastMessageExist = isLastMessageExist,
    lastMessageType = lastMessageType.toState()
)

fun RoomType.toState(): RoomState.RoomTypeState = when (this) {
    RoomType.DIRECT -> RoomState.RoomTypeState.DIRECT
    RoomType.PUBLIC_CHANNEL -> RoomState.RoomTypeState.PUBLIC_CHANNEL
    RoomType.PRIVATE_CHANNEL -> RoomState.RoomTypeState.PRIVATE_CHANNEL
    RoomType.DISCUSSIONS -> RoomState.RoomTypeState.DISCUSSIONS
    RoomType.TEAMS -> RoomState.RoomTypeState.TEAMS
    RoomType.LIVECHAT -> RoomState.RoomTypeState.LIVECHAT
    RoomType.VOIP -> RoomState.RoomTypeState.VOIP
    RoomType.UNKNOWN -> RoomState.RoomTypeState.UNKNOWN
}


fun RoomEntity.LastMessageType.toState(): RoomState.LastMessageType = when (this) {
    RoomEntity.LastMessageType.TEXT -> RoomState.LastMessageType.TEXT
    RoomEntity.LastMessageType.IMAGE -> RoomState.LastMessageType.IMAGE
    RoomEntity.LastMessageType.VIDEO -> RoomState.LastMessageType.VIDEO
    RoomEntity.LastMessageType.DOCUMENT -> RoomState.LastMessageType.DOCUMENT
    RoomEntity.LastMessageType.UNKNOWN -> RoomState.LastMessageType.UNKNOWN
}