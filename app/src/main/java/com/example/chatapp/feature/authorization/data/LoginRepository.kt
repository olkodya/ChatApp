package com.example.chatapp.feature.authorization.data

import com.example.chatapp.feature.authorization.data.model.LoginResponse

interface LoginRepository {

    suspend fun login(username: String, password: String): LoginResponse
}
