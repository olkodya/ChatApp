package com.example.chatapp.feature.authorization.domain

import com.example.chatapp.feature.authorization.data.LoginRepository
import com.example.chatapp.feature.authorization.data.model.LoginData
import com.example.chatapp.feature.authorization.data.model.toEntity
import com.example.chatapp.navigation.BottomNavigationItem
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
            Result.failure(e)
        }
    }
}