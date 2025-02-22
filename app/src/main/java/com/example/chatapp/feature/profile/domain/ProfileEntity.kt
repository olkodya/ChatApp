package com.example.chatapp.feature.profile.domain

import com.example.chatapp.feature.profile.presentation.ProfileState

data class ProfileEntity(
    val name: String,
    val avatarUrl: String,
)


fun ProfileEntity.toState(): ProfileState =
    ProfileState(
        name = name,
        imageUrl = avatarUrl,
    )
