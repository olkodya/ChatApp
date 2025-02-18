package com.example.chatapp.feature.authorization.domain

import android.util.Log
import com.example.chatapp.feature.authorization.data.AuthPreferences
import jakarta.inject.Inject
import kotlinx.coroutines.flow.first


class CheckAuthUseCaseImpl @Inject constructor(
    private val authPreferences: AuthPreferences
) : CheckAuthUseCase {

    override suspend operator fun invoke(): Boolean {
        return authPreferences.authData.first()?.userId != null
    }
}
