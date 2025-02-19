package com.example.chatapp.navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.chatapp.feature.authorization.presentation.AuthViewModel
import com.example.chatapp.feature.authorization.presentation.LoginScreen
import com.example.chatapp.feature.coinDetail.presentation.CoinDetailScreen

@Composable
fun RootNavGraph(navHostController: NavHostController) {

    val authViewModel: AuthViewModel = hiltViewModel()
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()

    LaunchedEffect(isAuthenticated, isLoading) {
        when {
            isLoading -> {
                Log.d("Navigation", "Loading...")
            }
            isAuthenticated -> {
                Log.d("Navigation", "Navigating to Main")
                navHostController.navigate(Routes.ScreenMain) {
                    popUpTo(0) { inclusive = true }
                }
            }
            else -> {
                Log.d("Navigation", "Navigating to Login")
                navHostController.navigate(Routes.ScreenLogin) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }


}

    NavHost(
        navController = navHostController, startDestination = Routes.ScreenSplash,
    ) {

        composable<Routes.ScreenSplash> {
            SplashScreen()

        }

        composable<Routes.ScreenLogin> {
            LoginScreen(onNavigateToMain = {
                navHostController.navigate(Routes.ScreenMain) {
                    popUpTo(Routes.ScreenLogin) { inclusive = true }
                }
            })
        }

        composable<Routes.ScreenMain> {
            MainScreen(navHostController)
        }

        composable<Routes.ScreenDetail> { backStackEntry ->
            val coin = requireNotNull(backStackEntry.toRoute<Routes.ScreenDetail>())
            CoinDetailScreen(
                coinId = coin.coinId,
                coinName = coin.coinName,
                coinPrice = coin.priceUsd.toBigDecimal(),
            ) { navHostController.popBackStack() }
        }
    }
}

@Composable
private fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}