package com.example.chatapp.feature.profile.data.model

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
    //val settings: Settings
//    @SerialName("customFields")
//    val customFields: CustomFields?,
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

@Serializable
data class CustomFields(
    @SerialName("twitter")
    val twitter: String,
)
