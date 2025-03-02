package com.example.chatapp.feature.chatList.data.model

import com.example.chatapp.feature.chatCreation.domain.model.CreateChatEntity
import kotlinx.serialization.Serializable

@Serializable
data class CreateChatResponse(
    val room: Room? = null,
    val success: Boolean? = null,
) {
    @Serializable
    data class Room(
        val t: String,
        val rid: String,
        val usernames: List<String>,
    )
}

