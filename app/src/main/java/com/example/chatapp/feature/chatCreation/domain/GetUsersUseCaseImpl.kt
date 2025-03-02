package com.example.chatapp.feature.chatCreation.domain

import com.example.chatapp.feature.chatCreation.domain.model.UserEntity
import com.example.chatapp.feature.chatList.data.ChatListRepositoryImpl
import com.example.chatapp.feature.chatList.data.model.toEntity
import javax.inject.Inject

class GetUsersUseCaseImpl @Inject constructor(private val repositoryImpl: ChatListRepositoryImpl) :
    GetUsersUseCase {
    override suspend fun invoke(): List<UserEntity> = repositoryImpl.getUsers().users.map {
        it.toEntity()
    }
}
