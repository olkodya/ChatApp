package com.example.chatapp.feature.chatList.domain

import com.example.chatapp.feature.chatList.data.ChatListRepositoryImpl
import com.example.chatapp.feature.chatList.data.model.toEntity
import com.example.chatapp.feature.chatList.domain.model.UserEntity
import javax.inject.Inject

class GetUsersUseCaseImpl @Inject constructor(private val repositoryImpl: ChatListRepositoryImpl) :
    GetUsersUseCase {
    override suspend fun invoke(): List<UserEntity> = repositoryImpl.getUsers().users.map {
        it.toEntity()
    }
}