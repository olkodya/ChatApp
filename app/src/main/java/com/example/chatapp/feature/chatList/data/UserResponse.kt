package com.example.chatapp.feature.chatList.data

import com.example.chatapp.feature.chatCreation.domain.model.UserEntity
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
    val utcOffset: Int? = null,
    val canViewAllInfo: Boolean? = false
)

@Serializable
data class UserListResponse(
    val users: List<UserInfo>
)

fun UserInfo.toEntity(): UserEntity = UserEntity(
    id = id,
    username = username,
    name = name,
)