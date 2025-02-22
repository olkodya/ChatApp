package com.example.chatapp.feature.profile.domain

import com.example.chatapp.feature.authorization.data.AuthPreferences
import com.example.chatapp.feature.profile.data.ProfileRepository
import javax.inject.Inject

class LogoutUseCaseImpl @Inject constructor(
    private val repository: ProfileRepository,
    private val authPreferences: AuthPreferences,
) : LogoutUseCase {

    override suspend fun invoke() {
        repository.logout()
        authPreferences.clear()
    }
}
