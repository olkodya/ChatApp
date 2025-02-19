package com.example.chatapp.feature.profile.data.model

import android.os.Message
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class LogoutResponse(
    @SerialName("status")
    val status: String,
    @SerialName("data")
    val data: LogoutData,
)

@Serializable
data class LogoutData(
    @SerialName("message")
    val message: String,
)