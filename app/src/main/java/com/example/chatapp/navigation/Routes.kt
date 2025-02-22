package com.example.chatapp.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {

    @Serializable
    object ScreenMain : Routes()

    @Serializable
    data class ScreenDetail(
        val coinId: String,
        val coinName: String,
        val priceUsd: String,
    ) : Routes()

    @Serializable
    object ScreenLogin : Routes()

    @Serializable
    object ScreenSplash : Routes()
}
