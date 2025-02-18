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
            R.drawable.chat_bubble,
            R.string.bottom_nav_title_chat
        )

    @Serializable
    data object Profile :
        BottomNavigationItem(
            R.drawable.account_circle,
            R.string.bottom_nav_title_profile
        )
}

