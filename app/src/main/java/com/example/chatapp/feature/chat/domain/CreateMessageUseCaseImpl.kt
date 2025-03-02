package com.example.chatapp.feature.chat.domain

import com.example.chatapp.feature.chat.data.ChatRepository
import javax.inject.Inject


class CreateMessageUseCaseImpl @Inject constructor(
    private val repository: ChatRepository
) : CreateMessageUseCase {
    override suspend fun invoke(roomId: String, text: String) = repository.sendMessage(
        roomId = roomId,
        text = text
    )
}
