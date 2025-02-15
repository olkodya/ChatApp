package com.example.coincapapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.coincapapp.feature.coinDetail.presentation.CoinDetailScreen

@Composable
fun RootNavGraph(navHostController: NavHostController) {
    NavHost(
        navController = navHostController, startDestination = Routes.ScreenMain,
    ) {

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