package com.example.chatapp.feature.chatCreation.domain.model

import com.example.chatapp.feature.chatList.data.model.CreateChatResponse
import kotlinx.serialization.Serializable

@Serializable
data class CreateChatEntity(
    val roomId: String,
)

fun CreateChatResponse.toEntity() = CreateChatEntity(
    roomId = requireNotNull(room?.rid)
)
