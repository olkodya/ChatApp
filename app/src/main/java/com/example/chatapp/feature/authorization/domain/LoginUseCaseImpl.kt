package com.example.chatapp.feature.authorization.domain

import com.example.chatapp.feature.authorization.data.LoginRepository
import com.example.chatapp.feature.authorization.data.model.toEntity
import javax.inject.Inject

class LoginUseCaseImpl @Inject constructor(
    private val repository: LoginRepository
): LoginUseCase {
    override suspend fun invoke(
        username: String,
        password: String
    ): LoginEntity = repository.login(username, password).toEntity()

}