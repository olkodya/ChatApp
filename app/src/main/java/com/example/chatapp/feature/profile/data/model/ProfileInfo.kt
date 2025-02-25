package com.example.chatapp.feature.profile.data.model

import com.example.chatapp.feature.profile.domain.ProfileEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileInfo(
    @SerialName("_id")
    val _id: String,
    @SerialName("name")
    val name: String,
    @SerialName("emails")
    val emails: List<Email>,
    @SerialName("status")
    val status: String,
    @SerialName("statusConnection")
    val statusConnection: String,
    @SerialName("username")
    val username: String,
    @SerialName("utcOffset")
    val utcOffset: Int,
    @SerialName("active")
    val active: Boolean,
    @SerialName("roles")
    val roles: List<String>,
    @SerialName("avatarUrl")
    val avatarUrl: String,
    @SerialName("success")
    val success: Boolean
)

@Serializable
data class Email(
    @SerialName("address")
    val address: String,
    @SerialName("verified")
    val verified: Boolean,
)

fun ProfileInfo.toEntity(userId: String, token: String): ProfileEntity = ProfileEntity(
    name = name,
//    avatarUrl = "${avatarUrl}?rc_uid=${userId}&rc_token=${token}",
    avatarUrl = avatarUrl
)
