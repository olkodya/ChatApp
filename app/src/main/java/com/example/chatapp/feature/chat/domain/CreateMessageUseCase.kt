package com.example.chatapp.feature.chat.domain

interface CreateMessageUseCase {
    suspend operator fun invoke(roomId: String, text: String)
}