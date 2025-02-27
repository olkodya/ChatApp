package com.example.chatapp.feature.chatList.domain

import com.example.chatapp.feature.chatList.domain.model.UserEntity

interface GetUsersUseCase {
    suspend operator fun invoke(): List<UserEntity>
}
