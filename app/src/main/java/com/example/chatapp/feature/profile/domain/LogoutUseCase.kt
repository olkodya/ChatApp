package com.example.chatapp.feature.profile.domain

import com.example.chatapp.feature.profile.data.model.LogoutResponse

interface LogoutUseCase {
    suspend operator fun invoke(): LogoutResponse
}