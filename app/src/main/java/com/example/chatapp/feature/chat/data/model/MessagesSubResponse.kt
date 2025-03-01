package com.example.chatapp.feature.chat.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessagesSubResponse(
    val msg: String,
    val collection: String,
    val id: String,
    val fields: Fields,
) {

    @Serializable
    data class Fields(
        @SerialName("args")
        val messages: List<@Serializable(with = MessageSerializer::class) MessageResponse>?,
    )
}


