package com.example.chatapp.feature.chat.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostMessageRequest(
    @SerialName("message")
    val message: TextMessage
)


