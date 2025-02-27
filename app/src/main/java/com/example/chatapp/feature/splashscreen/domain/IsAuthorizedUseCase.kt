package com.example.chatapp.feature.splashscreen.domain

interface IsAuthorizedUseCase {

    suspend operator fun invoke(): Boolean
}
