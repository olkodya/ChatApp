package com.example.chatapp.feature.chat.domain.model

import com.example.chatapp.feature.chat.data.model.RoomInfoResponse

data class ChatInfoEntity(
    val username: String? = null,
    val chatType: String? = null,
    val userId: String? = null,
    val chatName: String? = null,
    val chatAvatarUrl: String? = null,
    val numberOfMembers: Int? = null,
)


fun RoomInfoResponse.toEntity(usernameMe: String?, loggedUserId: String): ChatInfoEntity {
    val userName = room.usernames?.size?.let {
        if (it > 1) {
            if (room.usernames[0] != usernameMe)
                room.usernames[0]
            else {
                room.usernames[1]
            }
        } else {
            usernameMe
        }
    }

    val uid = room.uids?.firstOrNull()
    return ChatInfoEntity(
        userId = uid,
        username = userName,
        chatType = room.type,
        chatName = room.name ?: "",
        numberOfMembers = room.usersCount,
    )
}