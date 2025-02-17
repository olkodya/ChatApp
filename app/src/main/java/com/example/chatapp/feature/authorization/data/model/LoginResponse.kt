package com.example.chatapp.feature.authorization.data.model

import com.example.chatapp.feature.authorization.domain.LoginEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("status")
    val status: String,
    @SerialName("data")
    val data: LoginData,
)

@Serializable
data class LoginData(
    @SerialName("userId")
    val userId: String,
    @SerialName("authToken")
    val authToken: String,
    @SerialName("me")
    val me: UserInfo,
)

@Serializable
data class UserInfo(
    @SerialName("userId")
    val _id: String,
    @SerialName("username")
    val username: String,
    @SerialName("name")
    val name: String?,
)

fun LoginResponse.toEntity() : LoginEntity = LoginEntity()