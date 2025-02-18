package com.example.chatapp.feature.authorization.domain

import android.util.Log
import com.example.chatapp.feature.authorization.data.AuthPreferences
import com.example.chatapp.feature.authorization.data.LoginRepository
import com.example.chatapp.feature.authorization.data.model.LoginData
import com.example.chatapp.feature.authorization.presentation.InvalidCredentialsException
import kotlinx.coroutines.flow.collectLatest

import javax.inject.Inject

class LoginUseCaseImpl @Inject constructor(
    private val repository: LoginRepository,
    private val authPreferences: AuthPreferences,
) : LoginUseCase {

    override suspend fun invoke(
        username: String,
        password: String
    ): Result<LoginData> {
        return try {
            val response = repository.login(username, password)
            response.data.let { loginData ->
                if (!loginData.authToken.isNullOrEmpty() && !loginData.userId.isNullOrEmpty()) {
                    authPreferences.saveAuthData(
                        token = loginData.authToken,
                        userId = loginData.userId,
                    )
                    Log.d("dsd", loginData.userId)
                    Log.d("dsd", loginData.authToken)
                }
            }
            Result.success(response.data)
        } catch (e: Exception) {
            if (e.message.toString().contains("HTTP 401"))
                Result.failure(InvalidCredentialsException())
            else
                Result.failure(e)
        }
    }
}
