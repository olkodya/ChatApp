package com.example.chatapp.feature.authorization.domain

interface CheckAuthUseCase {
    suspend operator fun invoke(): Boolean
}
