package com.example.chatapp.feature.authorization.data

import com.example.chatapp.feature.authorization.data.api.LoginApi
import com.example.chatapp.feature.authorization.data.model.LoginRequest
import com.example.chatapp.feature.authorization.data.model.LoginResponse
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val api: LoginApi
) : LoginRepository {

    override suspend fun login(username: String, password: String): LoginResponse =
        api.loginByUsernameAndPassword(LoginRequest(user = username, password = password))
}
