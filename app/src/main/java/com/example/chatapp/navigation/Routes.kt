package com.example.chatapp.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {

    @Serializable
    object ScreenMain : Routes()

    @Serializable
    object ScreenChat : Routes()

    @Serializable
    object ScreenLogin : Routes()
}
