package com.example.chatapp.feature.chatList.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateChatRequest(
    @SerialName("username")
    val username: String,
)
