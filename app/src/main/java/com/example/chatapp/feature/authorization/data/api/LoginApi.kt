package com.example.chatapp.feature.authorization.data.api

import com.example.chatapp.feature.authorization.data.model.LoginRequest
import com.example.chatapp.feature.authorization.data.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    @POST("/api/v1/login")
    suspend fun loginByUsernameAndPassword(@Body loginRequest: LoginRequest): LoginResponse
}