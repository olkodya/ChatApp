package com.example.chatapp.feature.chatList.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class RoomsResponse(
    @SerialName("id")
    val id: String,
    @SerialName("msg")
    val msg: String,
    @SerialName("result")
    val result: Result,
) {

    @Serializable
    data class Result(
        @SerialName("update")
        val update: List<RoomResponse>
    )
}

@Serializable
data class RoomsResponseSubscription(
    val msg: String,
    val collection: String,
    val id: String,
    val fields: Fields
) {
    @Serializable
    data class Fields(
        val eventName: String,
        val args: List<JsonElement>
    )
}

@Serializable
data class RoomResponse(
    @SerialName("_id")
    val id: String,
    @SerialName("t")
    val type: String,
    @SerialName("name")
    val name: String? = null,
    @SerialName("usernames")
    val usernames: List<String>? = null,
    @SerialName("msgs")
    val msgs: Int,
    @SerialName("ts")
    val ts: Date? = null,
    @SerialName("uids")
    val uids: List<String>? = null,
    @SerialName("lastMessage")
    val lastMessage: LastMessageResponse? = null,
) {

    @Serializable
    data class LastMessageResponse(
        @SerialName("_id")
        val id: String,
        @SerialName("_updatedAt")
        val updatedAt: Date,
        @SerialName("md")
        val message: List<MessageResponse>? = null,
        @SerialName("u")
        val author: MessageAuthorResponse,
        @SerialName("file")
        val file: File? = null,
        @SerialName("msg")
        val msg: String? = null
    ) {

        @Serializable
        data class File(
            @SerialName("_id")
            val id: String,
            @SerialName("type")
            val type: String,
        )

        @Serializable
        data class MessageResponse(
            @SerialName("type")
            val type: String,
//            @SerialName("value")
//            val value: List<MessageValueResponse>? = null
        ) {
            @Serializable
            data class MessageValueResponse(
                @SerialName("type")
                val type: String? = null,
                @SerialName("value")
                val value: String? = null,
            )
        }

        @Serializable
        data class MessageAuthorResponse(
            @SerialName("_id")
            val id: String,
            @SerialName("username")
            val username: String,
            @SerialName("name")
            val name: String,
        )
    }
}
