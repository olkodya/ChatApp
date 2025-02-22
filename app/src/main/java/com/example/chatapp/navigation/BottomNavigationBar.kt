package com.example.chatapp.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(
    navController: NavHostController, modifier: Modifier
) {
    val screens = listOf(
        BottomNavigationItem.Chats,
        BottomNavigationItem.Profile,
    )

    NavigationBar(
        modifier = modifier.shadow(
            elevation = 6.dp,
        ),
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.let {
            when (it.route) {
                BottomNavigationItem.Chats::class.qualifiedName -> BottomNavigationItem.Chats
                else -> BottomNavigationItem.Profile
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
                    if (currentRoute == screen) {
                        Icon(
                            painter = painterResource(id = screen.selectedIcon),
                            contentDescription = stringResource(screen.title),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = screen.unselectedIcon),
                            contentDescription = stringResource(screen.title),
                            tint = com.example.chatapp.ui.theme.DarkGray
                        )
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedTextColor = MaterialTheme.colorScheme.secondary,
                    unselectedTextColor = com.example.chatapp.ui.theme.DarkGray
                )
            )
        }
    }
}
