package com.example.chatapp.feature.chatList.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponse(
    val user: UserInfo,
    val success: Boolean
)

@Serializable
data class UserInfo(
    @SerialName("_id")
    val id: String,
    val username: String,
    val type: String,
    val status: String,
    val active: Boolean,
    val name: String,
    val utcOffset: Int,
    val canViewAllInfo: Boolean
)