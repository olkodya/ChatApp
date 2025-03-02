package com.example.chatapp.feature.chat.domain

import com.example.chatapp.feature.chat.domain.model.ChatInfoEntity


interface GetRoomInfoUseCase {
    suspend operator fun invoke(roomId: String): ChatInfoEntity
}
