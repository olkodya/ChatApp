package com.example.chatapp.navigation

import com.example.chatapp.R
import kotlinx.serialization.Serializable

@Serializable
sealed class BottomNavigationItem(
    val icon: Int,
    val title: Int,
) {

    @Serializable
    data object Chats :
        BottomNavigationItem(
            R.drawable.assets_icon,
            R.string.bottom_nav_title_chat
        )

    @Serializable
    data object Profile :
        BottomNavigationItem(
            R.drawable.exchanges_icon,
            R.string.bottom_nav_title_profile
        )
}

