package com.example.chatapp.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {
    @Serializable
    object ScreenMain

    @Serializable
    data class ScreenDetail(
        val coinId: String,
        val coinName: String,
        val priceUsd: String,
    )

    @Serializable
    object ScreenLogin

    @Serializable
    object ScreenSplash


}
