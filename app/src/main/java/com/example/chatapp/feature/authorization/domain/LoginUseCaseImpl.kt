package com.example.chatapp.feature.authorization.domain

import com.example.chatapp.feature.authorization.data.AuthPreferences
import com.example.chatapp.feature.authorization.data.LoginRepository
import com.example.chatapp.feature.authorization.data.model.toEntity
import javax.inject.Inject

class LoginUseCaseImpl @Inject constructor(
    private val repository: LoginRepository,
    private val authPreferences: AuthPreferences,
) : LoginUseCase {

    override suspend fun invoke(
        username: String,
        password: String
    ): Result<LoginEntity> {

        return try {
            val response = repository.login(username, password)
            response.data.let { loginData ->
                if (!loginData.authToken.isNullOrEmpty() && !loginData.userId.isNullOrEmpty()) {
                    authPreferences.saveAuthData(
                        token = loginData.authToken,
                        userId = loginData.userId,
                    )
                }
            }
            Result.success(response.toEntity())
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}
