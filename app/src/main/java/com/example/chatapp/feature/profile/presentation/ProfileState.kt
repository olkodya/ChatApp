package com.example.chatapp.feature.profile.presentation

import androidx.compose.runtime.Immutable

@Immutable
data class ProfileState(
    val imageUrl: String,
    val name: String,
)