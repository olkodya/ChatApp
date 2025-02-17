package com.example.chatapp.feature.authorization.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerialName("user")
    val user: String,
    @SerialName("password")
    val password: String,
)