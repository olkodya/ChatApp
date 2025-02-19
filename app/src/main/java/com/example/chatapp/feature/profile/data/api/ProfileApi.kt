package com.example.chatapp.feature.profile.data.api

import com.example.chatapp.feature.authorization.data.model.LoginRequest
import com.example.chatapp.feature.authorization.data.model.LoginResponse
import com.example.chatapp.feature.profile.data.model.LogoutResponse
import com.example.chatapp.feature.profile.data.model.ProfileInfo
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProfileApi {
    @GET("/api/v1/me")
    suspend fun getUserInfo(): ProfileInfo

    @POST("api/v1/logout")
    suspend fun logout(): LogoutResponse
}