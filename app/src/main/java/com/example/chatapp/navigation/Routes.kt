package com.example.chatapp.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {

    @Serializable
    object ScreenMain : Routes()

    @Serializable
    data class ScreenChat(
        val roomId: String,
    ) : Routes() {

        companion object {
            const val ROOM_ID_ARG_KEY = "roomId"
        }
    }

    @Serializable
    object ScreenLogin : Routes()
}
