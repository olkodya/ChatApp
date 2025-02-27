package com.example.chatapp.feature.splashscreen.domain

import com.example.chatapp.feature.authorization.data.AuthPreferences
import jakarta.inject.Inject

class IsAuthorizedUseCaseImpl @Inject constructor(
    private val authPreferences: AuthPreferences
) : IsAuthorizedUseCase {

    override suspend operator fun invoke(): Boolean {
        return try {
            authPreferences.getAuthData() != null
        } catch (_: Exception) {
            false
        }
    }
}
