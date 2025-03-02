package com.example.chatapp.feature.chat.domain

import com.example.chatapp.feature.chat.data.ChatRepository
import com.example.chatapp.feature.chat.domain.model.ChatInfoEntity
import jakarta.inject.Inject

class GetRoomInfoUseCaseImpl @Inject constructor(private val chatRepository: ChatRepository) :
    GetRoomInfoUseCase {
    override suspend fun invoke(roomId: String): ChatInfoEntity {
        var chat = chatRepository.getRoomInfo(roomId = roomId)
        if (chat.chatType != "c") {
            runCatching {
                return chatRepository.getUserInfo(userId = chat.userId ?: "")
            }.onFailure {
                print(it)
            }
        }
        return chat
    }
}
