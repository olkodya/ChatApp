package com.example.chatapp.feature.chatCreation.domain

import com.example.chatapp.feature.chatCreation.domain.model.UserEntity

interface GetUsersUseCase {
    suspend operator fun invoke(): List<UserEntity>
}