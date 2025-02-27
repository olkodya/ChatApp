package com.example.chatapp.feature.chatList.data.model

import com.example.chatapp.feature.authorization.data.AuthData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient


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
