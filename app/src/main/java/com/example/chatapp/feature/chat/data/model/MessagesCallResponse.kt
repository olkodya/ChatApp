package com.example.chatapp.feature.chat.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MessagesCallResponse(
    val msg: String,
    val id: String?,
    val result: MessagesResult?
)

@Serializable
data class MessagesResult(
    val messages: List<@Serializable(with = MessageSerializer::class) MessageResponse>?,
    val unreadNotLoaded: Int?
)
