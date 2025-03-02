package com.example.chatapp.feature.chatCreation.domain

import com.example.chatapp.feature.chatCreation.domain.model.CreateChatEntity

interface CreateChatUseCase {

    suspend operator fun invoke(username: String): CreateChatEntity
}