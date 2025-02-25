package com.example.chatapp.feature.chatList.domain.model

import com.example.chatapp.feature.chatList.data.model.RoomResponse
import com.example.chatapp.feature.chatList.domain.model.RoomEntity.LastMessageEntity.MessageEntity
import com.example.chatapp.feature.chatList.presentation.RoomState

data class RoomEntity(
    val id: String,
    val type: String,
    val name: String?,
    val lastMessage: LastMessageEntity,
) {

    data class LastMessageEntity(
        val id: String,
        val updatedAt: Long,
        val message: MessageEntity?,
        val author: MessageAuthorEntity,
    ) {

        data class MessageEntity(
            val type: String,
            val value: String,
        )

        data class MessageAuthorEntity(
            val id: String,
            val username: String,
            val name: String,
        )
    }
}

fun RoomResponse.toEntity() = RoomEntity(
    id = id,
    name = name,
    type = type,
    lastMessage = RoomEntity.LastMessageEntity(
        id = lastMessage.id,
        updatedAt = lastMessage.updatedAt.date,
        message = lastMessage.message?.firstOrNull()?.let { message ->
            MessageEntity(
                type = message.value.firstOrNull()?.type ?: return@let null,
                value = message.value.firstOrNull()?.value ?: return@let null
            )
        },
        author = RoomEntity.LastMessageEntity.MessageAuthorEntity(
            id = lastMessage.author.id,
            username = lastMessage.author.username,
            name = lastMessage.author.name,
        )
    )
)

fun RoomEntity.toRoomState() = RoomState(
    id = id,
    imageUrl = "",
    type = type,
    name = name ?: "",
    lastMassage = lastMessage.message?.value ?: "",
    lastMessageDate = lastMessage.updatedAt.toString()
)
