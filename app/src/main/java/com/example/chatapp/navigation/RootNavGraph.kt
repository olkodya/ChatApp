package com.example.chatapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.chatapp.feature.authorization.presentation.AuthViewModel
import com.example.chatapp.feature.authorization.presentation.LoginScreen
import com.example.chatapp.feature.coinDetail.presentation.CoinDetailScreen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RootNavGraph(navHostController: NavHostController) {

    val authViewModel: AuthViewModel = hiltViewModel()
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()

    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated) {
            navHostController.navigate(Routes.ScreenMain) {
                popUpTo(Routes.LoginScreen) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navHostController, startDestination = Routes.LoginScreen,
    ) {

        composable<Routes.LoginScreen> {
            LoginScreen(onNavigateToMain = {
                navHostController.navigate(Routes.ScreenMain) {
                    popUpTo(Routes.LoginScreen) { inclusive = true }
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