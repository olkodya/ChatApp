package com.example.chatapp.feature.authorization.domain

import com.example.chatapp.feature.authorization.data.model.LoginData


interface LoginUseCase {

    suspend operator fun invoke(username: String, password: String): Result<LoginEntity>

}