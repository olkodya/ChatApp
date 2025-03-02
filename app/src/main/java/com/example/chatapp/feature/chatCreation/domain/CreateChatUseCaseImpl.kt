package com.example.chatapp.feature.chatCreation.domain

import com.example.chatapp.feature.chatCreation.domain.model.CreateChatEntity
import com.example.chatapp.feature.chatCreation.domain.model.toEntity
import com.example.chatapp.feature.chatList.data.ChatListRepository
import javax.inject.Inject

class CreateChatUseCaseImpl @Inject constructor(
    private val repository: ChatListRepository
) : CreateChatUseCase {
    override suspend fun invoke(username: String): CreateChatEntity =
        repository.createChat(username).toEntity()
}
