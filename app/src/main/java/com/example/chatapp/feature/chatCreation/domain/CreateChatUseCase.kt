package com.example.chatapp.feature.chatCreation.domain

interface CreateChatUseCase {

    suspend operator fun invoke(username: String)
}