package com.example.chatapp.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScreen(rootNavHostController: NavHostController) {
    val mainNavHostController: NavHostController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(snackbarData = data)
            }
        },
        bottomBar = {
            BottomNavigationBar(
                navController = mainNavHostController,
                modifier = Modifier
            )
        }
    ) { paddingValues ->
        MainNavGraph(paddingValues, mainNavHostController, rootNavHostController)
    }
}
