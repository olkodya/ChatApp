package com.example.chatapp.feature.chatList.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class SubscriptionsResponse(
    @SerialName("msg")
    val msg: String,
    @SerialName("id")
    val id: String,
    @SerialName("result")
    val result: Result,
)

@Serializable
data class Result(
    @SerialName("update")
    val update: List<SubscriptionResponse>
)


@Serializable
data class SubscriptionsSubscriptionResponse(
    @SerialName("msg")
    val msg: String,
    @SerialName("collection")
    val collection: String,
    val id: String,
    val fields: Fields,
) {
    @Serializable
    data class Fields(
        val eventName: String,
        val args: List<JsonElement>,
    )
}

@Serializable
data class SubscriptionResponse(
//    @SerialName("_id")
//    val id: String,
    @SerialName("rid")
    val rid: String,
//    @SerialName("u")
//    val user: User,
//    @SerialName("_updatedAt")
//    val updatedAt: Date,
//    val alert: Boolean,
//    val fname: String? = null,
//    val groupMentions: Int,
//    val name: String,
//    val open: Boolean,
//    val t: String,
//    val ts: Date,
    @SerialName("unread")
    val unread: Int,
//    val userMentions: Int,
//    @SerialNae("ls")
//    val ls: Damte,
)

@Serializable
data class User(
    @SerialName("_id")
    val id: String,
    val username: String,
)

@Serializable
data class Date(
    @SerialName("\$date")
    val date: Long
)