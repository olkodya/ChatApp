package com.example.chatapp.feature.chatList.domain.model

import com.example.chatapp.feature.chatList.data.model.RoomResponse
import com.example.chatapp.feature.chatList.presentation.RoomState

//data class RoomEntity(
//    val id: String,
//    val type: String,
//
//    val name: String?,
//    val lastMessage: LastMessageEntity,
//) {
//
//    data class LastMessageEntity(
//        val id: String,
//        val updatedAt: Long,
//        val message: MessageEntity?,
//        val author: MessageAuthorEntity,
//    ) {
//
//        data class MessageEntity(
//            val type: String,
//            val value: String,
//        )
//
//        data class MessageAuthorEntity(
//            val id: String,
//            val username: String,
//            val name: String,
//        )
//    }
//}


data class RoomEntity(
    val id: String,
    val type: String,
    val userName: String?,
    val name: String?,
    val userId: String?,
    val lastMessageContent: String?,
    val lastMessageAuthor: String?,
    val lastMessageAuthorId: String,
    val lastMessageDate: String?,
    val isMeMessageAuthor: Boolean,
    val unreadMessagesNumber: Int,
)

fun RoomResponse.toEntity(unreadMessagesNumber: Int, userId: String): RoomEntity {
//    val name = if (type == "c") {
//        this.name
//    } else if (type == "d") {
//        "Username"
//    } else {
//        "...."
//    }

//    val lastMessageAuthor = if (type == "c") {
//        if (this.lastMessage.author.id == userId) {
//            "Вы"
//        } else {
//            this.lastMessage.author.name
//        }
//    } else {
//        null
//    }

//    val lastMessageStatus = if (this.lastMessage.author.id == userId) {
//        if (unreadMessagesNumber != 0) {
//            "unread"
//        } else {
//            "read"
//        }
//    } else {
//        ""
//    }
    return RoomEntity(
        id = id,
        name = name,
        userName = null, // Возможно затераем уже установленный userName c запроса на userInfo
        userId = uids?.firstOrNull(),
        type = type,
        lastMessageContent = lastMessage.message?.firstOrNull()?.value?.firstOrNull()?.value,
        lastMessageAuthor = lastMessage.author.name,
        lastMessageAuthorId = lastMessage.author.id,
        lastMessageDate = lastMessage.updatedAt.date.toString(),
        unreadMessagesNumber = unreadMessagesNumber,
        isMeMessageAuthor = userId == lastMessage.author.id,
    )
}


//fun RoomResponse.toEntity() = RoomEntity(
//    id = id,
//    name = name,
//    type = type,
//    lastMessage = RoomEntity.LastMessageEntity(
//        id = lastMessage.id,
//        updatedAt = lastMessage.updatedAt.date,
//        message = lastMessage.message?.firstOrNull()?.let { message ->
//            MessageEntity(
//                type = message.value.firstOrNull()?.type ?: return@let null,
//                value = message.value.firstOrNull()?.value ?: return@let null
//            )
//        },
//        author = RoomEntity.LastMessageEntity.MessageAuthorEntity(
//            id = lastMessage.author.id,
//            username = lastMessage.author.username,
//            name = lastMessage.author.name,
//        )
//    )
//)
//
fun RoomEntity.toRoomState() = RoomState(
    id = id,
    imageUrl = null,
    type = type,
    name = name,
    lastMassage = lastMessageContent,
    lastMessageDate = lastMessageDate,
    lastMessageAuthor = lastMessageAuthor,
    isMeMessageAuthor = isMeMessageAuthor,
    unreadMessagesNumber = unreadMessagesNumber,
)
