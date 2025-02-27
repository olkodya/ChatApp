package com.example.chatapp.feature.profile.presentation

import androidx.compose.runtime.Immutable
import com.example.chatapp.components.ErrorState

@Immutable
sealed class ProfileScreenState {

    @Immutable
    data class Content(
        val profileInfo: ProfileState
    ) : ProfileScreenState()

    @Immutable
    data object Loading : ProfileScreenState()

    @Immutable
    data class Error(val state: ErrorState) : ProfileScreenState()
}

@Immutable
data class ProfileState(
    val imageUrl: String,
    val name: String,
)
