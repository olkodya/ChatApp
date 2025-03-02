package com.example.chatapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.chatapp.feature.authorization.presentation.LoginScreen
import com.example.chatapp.feature.chat.presentation.ChatScreen
import com.example.chatapp.feature.chatList.presentation.RoomState

@Composable
fun RootNavGraph(
    navHostController: NavHostController,
    startDestination: Routes,
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
    ) {

        composable<Routes.ScreenLogin> {
            LoginScreen(
                onNavigateToMain = {
                    navHostController.navigate(Routes.ScreenMain) {
                        popUpTo(Routes.ScreenLogin) { inclusive = true }
                    }
                }
            )
        }

        composable<Routes.ScreenMain> {
            MainScreen(navHostController)
        }

        composable<Routes.ScreenChat> {
            val roomIdKey = Routes.ScreenChat.ROOM_ID_ARG_KEY
            val roomId = it.arguments?.getString(roomIdKey)
                ?: error("Routes.ScreenChat not contains $roomIdKey")
            val roomTypeKey = Routes.ScreenChat.ROOM_TYPE_ARG_KEY
            val roomType = it.arguments?.getString(roomTypeKey)?.let { typeStr ->
                RoomState.RoomTypeState.valueOf(typeStr)
            } ?: RoomState.RoomTypeState.UNKNOWN

            ChatScreen(
                roomId = roomId,
                roomType = roomType,
                navigateBack = {
                    navHostController.popBackStack()
                }
            )
        }
    }
}
