package com.example.chatapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = DarkBLue,
    secondary = LightBlue,
    tertiary = DarkGray,
    surfaceVariant = LightGray,
)

private val LightColorScheme = lightColorScheme(
    primary = DarkBLue,
    secondary = LightBlue,
    tertiary = DarkGray,
    surfaceVariant = LightGray,
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
