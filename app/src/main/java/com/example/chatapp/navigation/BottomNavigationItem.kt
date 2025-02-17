package com.example.chatapp.navigation

import com.example.chatapp.R
import kotlinx.serialization.Serializable

@Serializable
sealed class BottomNavigationItem(
    val icon: Int,
    val title: Int,
) {

    @Serializable
    data object Coins :
        BottomNavigationItem(R.drawable.assets_icon, R.string.assets_title)

    @Serializable
    data object Exchanges :
        BottomNavigationItem(R.drawable.exchanges_icon, R.string.exchanges)
}

