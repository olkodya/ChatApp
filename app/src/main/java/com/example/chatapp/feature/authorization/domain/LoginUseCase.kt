package com.example.chatapp.feature.authorization.domain


interface LoginUseCase {

    suspend operator fun invoke(username: String, password: String): Result<LoginEntity>
}
