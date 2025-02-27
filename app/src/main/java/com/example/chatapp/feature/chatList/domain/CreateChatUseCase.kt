package com.example.chatapp.feature.chatList.domain

import com.example.chatapp.feature.chatList.domain.model.RoomEntity
import kotlinx.coroutines.flow.StateFlow

interface CreateChatUseCase {

    suspend operator fun invoke(username: String)
}
