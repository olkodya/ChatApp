package com.example.coincapapp.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(
    navController: NavHostController, modifier: Modifier
) {
    val screens = listOf(
        BottomNavigationItem.Coins,
        BottomNavigationItem.Exchanges,
    )

    NavigationBar(
        modifier = modifier,
        containerColor = Color.Transparent,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.let {
            when (it.route) {
                BottomNavigationItem.Coins::class.qualifiedName -> BottomNavigationItem.Coins
                else -> BottomNavigationItem.Exchanges
            }
        }

        screens.forEach { screen ->
            NavigationBarItem(
                label = { Text(text = stringResource(screen.title)) },
                selected = currentRoute == screen,
                onClick = {
                    navController.navigate(screen) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = screen.icon),
                        contentDescription = stringResource(screen.title)
                    )
                },
            )
        }
    }
}
