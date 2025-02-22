package com.example.chatapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.chatapp.feature.chatList.presentation.ChatListScreen
import com.example.chatapp.feature.profile.presentation.ProfileScreen

@Composable
fun MainNavGraph(
    navHostController: NavHostController,
    rootNavHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = BottomNavigationItem.Chats
    ) {

        composable<BottomNavigationItem.Chats> {
            ChatListScreen()
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
