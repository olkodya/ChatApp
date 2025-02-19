package com.example.chatapp.feature.authorization.domain

data class LoginEntity(
    val userId: String?,
    val authToken: String?,
    val _id: String,
    val username: String,
    val name: String?,
    val status: String,
)
