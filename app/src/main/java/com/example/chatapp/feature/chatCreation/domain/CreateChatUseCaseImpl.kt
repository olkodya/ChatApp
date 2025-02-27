package com.example.chatapp.feature.chatCreation.domain

import com.example.chatapp.feature.chatList.data.ChatListRepository
import javax.inject.Inject

class CreateChatUseCaseImpl @Inject constructor(
    private val repository: ChatListRepository
) : CreateChatUseCase {
    override suspend fun invoke(username: String) = repository.createChat(username)
}