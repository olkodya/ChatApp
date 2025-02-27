package com.example.chatapp.feature.chatList.domain.model

import com.example.chatapp.feature.chatList.data.model.RoomResponse
import com.example.chatapp.feature.chatList.presentation.RoomState

data class RoomEntity(
    val id: String,
    val type: String,
    val userName: String?,
    val name: String?,
    val userId: String?,
    val lastMessageContent: String?,
    val lastMessageAuthor: String?,
    val lastMessageAuthorId: String,
    val lastMessageDate: Long?,
    val isMeMessageAuthor: Boolean,
    val unreadMessagesNumber: Int,
)

fun RoomResponse.toEntity(unreadMessagesNumber: Int, userId: String): RoomEntity {
    return RoomEntity(
        id = id,
        name = name,
        userName = null,
        userId = uids?.firstOrNull(),
        type = type,
        lastMessageContent = if (msgs == 0) "Сообщений нет" else lastMessage?.message?.firstOrNull()?.value?.firstOrNull()?.value,
        lastMessageAuthor = lastMessage?.author?.name,
        lastMessageAuthorId = lastMessage?.author?.id ?: "",
        lastMessageDate = lastMessage?.updatedAt?.date,
        unreadMessagesNumber = unreadMessagesNumber,
        isMeMessageAuthor = lastMessage != null && userId == lastMessage.author.id
    )
}

fun RoomEntity.toRoomState() = RoomState(
    id = id,
    imageUrl = null,
    type = type,
    name = name,
    userName = userName,
    lastMassage = lastMessageContent,
    lastMessageDate = lastMessageDate,
    lastMessageAuthor = lastMessageAuthor,
    isMeMessageAuthor = isMeMessageAuthor,
    unreadMessagesNumber = unreadMessagesNumber,
)
