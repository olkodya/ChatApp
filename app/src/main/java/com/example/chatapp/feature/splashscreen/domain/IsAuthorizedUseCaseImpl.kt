package com.example.chatapp.feature.splashscreen.domain

import android.util.Log
import com.example.chatapp.feature.authorization.data.AuthPreferences
import com.example.chatapp.feature.splashscreen.domain.IsAuthorizedUseCase
import jakarta.inject.Inject

class IsAuthorizedUseCaseImpl @Inject constructor(
    private val authPreferences: AuthPreferences
) : IsAuthorizedUseCase {

    override suspend operator fun invoke(): Boolean {
        return try {
            authPreferences.getAuthData() != null
        } catch (e: Exception) {
            Log.e("CheckAuthUseCase", "Error checking auth", e)
            false
        }
    }
}