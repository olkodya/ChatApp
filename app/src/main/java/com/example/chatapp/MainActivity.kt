package com.example.chatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.memory.MemoryCache
import coil3.request.crossfade
import com.example.chatapp.di.CoilInterceptor
import com.example.chatapp.feature.splashscreen.presentation.SplashViewModel
import com.example.chatapp.feature.splashscreen.presentation.SplashViewModel.SplashEvent
import com.example.chatapp.navigation.RootNavGraph
import com.example.chatapp.navigation.Routes
import com.example.chatapp.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val splashViewModel: SplashViewModel by viewModels()

    @Inject
    lateinit var coilInterceptor: CoilInterceptor

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setupSplashScreen(
            onDestinationDefined = { destination ->
                setContent {
                    SetupImageLoader(coilInterceptor)
                    AppTheme {
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

@Composable
private fun SetupImageLoader(coilInterceptor: CoilInterceptor) {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                add(coilInterceptor)
            }
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context = context, percent = 0.25)
                    .build()
            }
            .crossfade(false)
            .build()
    }
}
