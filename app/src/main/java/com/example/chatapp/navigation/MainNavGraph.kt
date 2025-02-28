package com.example.chatapp.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.chatapp.feature.chatList.presentation.ChatListScreen
import com.example.chatapp.feature.profile.presentation.ProfileScreen

@Composable
fun MainNavGraph(
    paddingValues: PaddingValues,
    navHostController: NavHostController,
    rootNavHostController: NavHostController
) {
    NavHost(
        modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
        navController = navHostController,
        startDestination = BottomNavigationItem.Chats
    ) {

        composable<BottomNavigationItem.Chats> {
            ChatListScreen(
                onNavigateToChat = { roomId ->
                    rootNavHostController.navigate(Routes.ScreenChat(roomId = roomId))
                }
            )
        }

        composable<BottomNavigationItem.Profile> {
            ProfileScreen(
                onNavigateToLogin = {
                    rootNavHostController.popBackStack()
                    rootNavHostController.navigate(Routes.ScreenLogin)
                }
            )
        }
    }
}
