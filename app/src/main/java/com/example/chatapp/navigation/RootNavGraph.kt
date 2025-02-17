package com.example.chatapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.chatapp.feature.authorization.presentation.LoginScreen
import com.example.chatapp.feature.coinDetail.presentation.CoinDetailScreen

@Composable
fun RootNavGraph(navHostController: NavHostController) {
    NavHost(
        navController = navHostController, startDestination = Routes.LoginScreen,
    ) {

        composable<Routes.LoginScreen>{
            LoginScreen(){

            }
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