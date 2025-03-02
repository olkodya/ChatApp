package com.example.chatapp.feature.chat.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class RoomInfoResponse(
    @SerialName("room")
    val room: Room,
){
    @Serializable
    data class Room(
        @SerialName("_id")
        val id: String,
        @SerialName("ts")
        val timestamp: String,
        @SerialName("t")
        val type: String,
        @SerialName("name")
        val name: String? = null,
        @SerialName("usernames")
        val usernames: List<String>? = null,
        @SerialName("uids")
        val uids: List<String>? = null,
        @SerialName("msgs")
        val messagesCount: Int,
        @SerialName("usersCount")
        val usersCount: Int,
        @SerialName("_updatedAt")
        val updatedAt: String,
        @SerialName("lm")
        val lastMessageTimestamp: String? = null,
    )
}