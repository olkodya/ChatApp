package com.example.chatapp.feature.chatList.data.model

import com.example.chatapp.feature.authorization.data.AuthData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonObject


@Serializable
sealed class WebSocketMessage(
    @SerialName("msg")
    open val msg: String,
) {
    @Serializable
    data class Connect(
        @SerialName("version")
        private val version: String = "1",
        @SerialName("support")
        private val support: List<String> = listOf("1"),
    ) : WebSocketMessage(msg = "connect")

    @Serializable
    data class Pong(
        @Transient
        override val msg: String = "pong"
    ) : WebSocketMessage(msg)

    @Serializable
    data class Authorize(
        @SerialName("method")
        private val method: String = "login",
        @SerialName("id")
        val id: String = "",
        @SerialName("params")
        val params: List<LoginParam>
    ) : WebSocketMessage(msg = "method") {

        @Serializable
        data class LoginParam(
            @SerialName("user")
            val user: User,
            @SerialName("password")
            val password: Password
        ) {

            @Serializable
            data class User(
                @SerialName("username")
                val username: String
            )

            @Serializable
            data class Password(
                @SerialName("digest")
                val digest: String,
                @SerialName("algorithm")
                val algorithm: String
            )
        }
    }

    @Serializable
    data class RoomsGet(
        @SerialName("method")
        private val method: String = "rooms/get",
        @SerialName("id")
        val id: String,
        @SerialName("params")
        val params: List<Date> = listOf(Date())
    ) : WebSocketMessage(msg = "method") {

        @Serializable
        data class Date(
            @SerialName("\$date")
            val date: Long = 0,
        )
    }

    @Serializable
    data class RoomsSubscribe(
        @SerialName("name")
        private val name: String = "stream-notify-user",
        @SerialName("id")
        val id: String,
        @SerialName("params")
        val params: List<String>,
    ) : WebSocketMessage(msg = "sub") {

        companion object {
            fun roomsFactory(id: String, userId: String) = RoomsSubscribe(
                id = id,
                params = listOf(
                    "$userId/rooms-changed",
                    "false",
                )
            )
        }
    }

    @Serializable
    data class SubscriptionsGet(
        @SerialName("method")
        val method: String = "subscriptions/get",
        @SerialName("id")
        val id: String,
        @SerialName("params")
        val params: List<Date> = listOf(Date())
    ) : WebSocketMessage(msg = "method") {
        @Serializable
        data class Date(
            @SerialName("\$date")
            val date: Long = 0,
        )
    }

    @Serializable
    data class SubscriptionsSubscribe(
        @SerialName("name")
        private val name: String = "stream-notify-user",
        @SerialName("id")
        val id: String,
        @SerialName("params")
        val params: List<String>,
    ) : WebSocketMessage(msg = "sub") {

        companion object {
            fun subscriptionsFactory(id: String, userId: String) = RoomsSubscribe(
                id = id,
                params = listOf(
                    "$userId/subscriptions-changed",
                    "false",
                )
            )
        }
    }

    @Serializable
    data class MessagesSubscribe(
        @SerialName("name")
        private val name: String = "stream-room-messages",
        @SerialName("id")
        val id: String,
        @SerialName("params")
        val params: List<@Serializable(with = ParamTypeSerializer::class) ParamType?>,
    ) : WebSocketMessage(msg = "sub") {

        companion object {
            fun messagesFactory(id: String, roomId: String) = MessagesSubscribe (
                id = id,
                params = listOf(
                    ParamType.StringValue(roomId),
                    ParamType.BoolValue(false),
                )
            )
        }
    }

    @Serializable
    data class LoadHistoryRequest(
        val method: String = "loadHistory",
        val id: String,
        val params: List<@Serializable(with = ParamTypeSerializer::class) ParamType?>
    ) : WebSocketMessage(msg = "method") {
        companion object {
            fun create(
                id: String,
                roomId: String,
                limit: Int = 50,
                date: Long = 0
            ) = LoadHistoryRequest(
                id = id,
                params = listOf(
                    ParamType.StringValue(roomId),
                    ParamType.NullValue,
                    ParamType.IntValue(limit),
                    ParamType.DateValue(date)
                )
            )
        }
    }

    @Serializable
    sealed class ParamType {
        @Serializable
        data class StringValue(val value: String) : ParamType()

        @Serializable
        data class IntValue(val value: Int) : ParamType()

        @Serializable
        data class BoolValue(val value: Boolean) : ParamType()

        @Serializable
        data class DateValue(
            val value: Long
        ) : ParamType()

        @Serializable
        data object NullValue : ParamType()
    }

    object ParamTypeSerializer : JsonTransformingSerializer<ParamType>(ParamType.serializer()) {
        override fun transformSerialize(element: JsonElement): JsonElement {
            return when (val param = element.jsonObject["value"]) {
                null -> JsonNull
                is JsonPrimitive -> param
                else -> JsonObject(mapOf("\$date" to param))
            }
        }
    }
}

fun AuthData.toWebSocketMessage() = WebSocketMessage.Authorize(
    params = listOf(
        WebSocketMessage.Authorize.LoginParam(
            user = WebSocketMessage.Authorize.LoginParam.User(
                username = username
            ),
            password = WebSocketMessage.Authorize.LoginParam.Password(
                digest = password,
                algorithm = "sha-256"
            )
        )
    )
)

