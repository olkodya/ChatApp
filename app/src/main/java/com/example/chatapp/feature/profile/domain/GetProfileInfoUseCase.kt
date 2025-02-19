package com.example.chatapp.feature.profile.domain

import com.example.chatapp.feature.profile.data.model.ProfileInfo

interface GetProfileInfoUseCase {
    suspend operator fun invoke() : ProfileEntity
}