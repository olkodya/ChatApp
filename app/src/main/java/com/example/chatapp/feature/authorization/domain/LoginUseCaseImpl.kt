package com.example.chatapp.feature.authorization.domain

import com.example.chatapp.feature.authorization.data.LoginRepository
import com.example.chatapp.feature.authorization.data.model.LoginData
import com.example.chatapp.feature.authorization.presentation.InvalidCredentialsException

import javax.inject.Inject

class LoginUseCaseImpl @Inject constructor(
    private val repository: LoginRepository
) : LoginUseCase {
    override suspend fun invoke(
        username: String,
        password: String
    ): Result<LoginData> {
        return try {
            val response = repository.login(username, password)
            Result.success(response.data)
        } catch (e: Exception) {
            if (e.message.toString().contains("HTTP 401"))
                Result.failure(InvalidCredentialsException())
            else
                Result.failure(e)
        }
    }
}