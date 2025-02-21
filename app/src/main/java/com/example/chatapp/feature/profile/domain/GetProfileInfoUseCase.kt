package com.example.chatapp.feature.profile.domain

interface GetProfileInfoUseCase {
    suspend operator fun invoke() : ProfileEntity
}