package com.example.chatapp.feature.chatList.domain.model

import com.example.chatapp.feature.chatList.presentation.UserState

data class UserEntity(
    val id: String,
    val username: String,
    val name: String,
)


fun UserEntity.toUserState(): UserState = UserState(
    id = id,
    avatarUrl = "https://eltex2025.rocket.chat/avatar/$username",
    name = name,
    username = username,
)