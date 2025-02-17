package com.example.chatapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.chatapp.feature.coinList.presentation.CoinListScreen
import com.example.chatapp.feature.exchangeList.presentation.ExchangeListScreen

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
            CoinListScreen(
                routeToCoinDetailScreen = { id, name, price ->
                    rootNavHostController.navigate(
                        Routes.ScreenDetail(
                            coinId = id,
                            coinName = name,
                            priceUsd = price.toString()
                        )
                    )
                },
            )
        }

        composable<BottomNavigationItem.Profile> {
            ExchangeListScreen()
        }
    }
}