package com.example.chatapp.feature.chatCreation.domain.model

import com.example.chatapp.feature.chatCreation.presentation.UserState

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