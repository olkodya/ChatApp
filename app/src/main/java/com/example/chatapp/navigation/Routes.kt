package com.example.chatapp.navigation

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.example.chatapp.feature.chatList.presentation.RoomState
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {

    @Serializable
    object ScreenMain : Routes()

    @Serializable
    data class ScreenChat(
        val roomId: String,
//        val roomType: RoomState.RoomTypeState,
    ) : Routes() {

        companion object {
            const val ROOM_ID_ARG_KEY = "roomId"
            const val ROOM_TYPE_ARG_KEY = "roomType"
        }
    }

    @Serializable
    object ScreenLogin : Routes()
}


