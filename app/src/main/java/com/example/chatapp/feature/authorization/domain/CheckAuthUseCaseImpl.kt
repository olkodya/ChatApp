package com.example.chatapp.feature.authorization.domain

import android.util.Log
import com.example.chatapp.feature.authorization.data.AuthPreferences
import jakarta.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.time.withTimeoutOrNull


class CheckAuthUseCaseImpl @Inject constructor(
    private val authPreferences: AuthPreferences
) : CheckAuthUseCase {

    override suspend operator fun invoke(): Boolean {
        return try {
            val result = authPreferences.authData
                .catch {
                    emit(null)
                }
                .first()
            result?.userId != null
        } catch (e: Exception) {
            Log.e("CheckAuthUseCase", "Error checking auth", e)
            false
        }
    }
}
