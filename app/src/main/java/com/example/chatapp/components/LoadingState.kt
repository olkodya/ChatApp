package com.example.chatapp.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoadingState(
    modifier: Modifier = Modifier,
    color: Color? = null
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        if (color!=null)
        CircularProgressIndicator(Modifier.size(24.dp), color = MaterialTheme.colorScheme.surface)
        else
            CircularProgressIndicator(Modifier.size(24.dp))

    }
}

@Composable
@Preview(showBackground = true)
fun LoadingStatePreview() {
    LoadingState(Modifier)
}
