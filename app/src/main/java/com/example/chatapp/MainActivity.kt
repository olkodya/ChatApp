package com.example.chatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.feature.splashscreen.presentation.SplashViewModel
import com.example.chatapp.feature.splashscreen.presentation.SplashViewModel.SplashEvent
import com.example.chatapp.navigation.RootNavGraph
import com.example.chatapp.navigation.Routes
import com.example.chatapp.ui.theme.CoinCapAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setupSplashScreen(
            onDestinationDefined = { destination ->
                setContent {
                    CoinCapAppTheme {
                        val navController = rememberNavController()
                        RootNavGraph(navController, destination)
                    }
                }
            },
        )
    }

    private fun setupSplashScreen(onDestinationDefined: (Routes) -> Unit) {
        var keepSplashScreenOn = true
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                splashViewModel.events.collect { event ->
                    when (event) {
                        SplashEvent.NavigateToLogin -> onDestinationDefined(Routes.ScreenLogin)
                        SplashEvent.NavigateToMain -> onDestinationDefined(Routes.ScreenMain)
                    }
                    keepSplashScreenOn = false
                }
            }
        }
        installSplashScreen().setKeepOnScreenCondition {
            keepSplashScreenOn
        }
    }
}
