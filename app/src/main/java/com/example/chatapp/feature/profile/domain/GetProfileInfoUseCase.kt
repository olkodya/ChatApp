package com.example.chatapp.feature.profile.domain

interface GetProfileInfoUseCase {

    suspend operator fun invoke(userId: String, token: String): ProfileEntity
}
