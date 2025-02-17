package com.example.chatapp.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EmptyState(
    modifier: Modifier = Modifier,
    message: String,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier.padding(bottom = 23.dp),
            text = message,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyStatePreview() {
    EmptyState(
        modifier = Modifier,
        message = "Coins not found"
    )
}