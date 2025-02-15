package com.example.coincapapp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.coincapapp.R

@Composable
fun ErrorState(
    modifier: Modifier = Modifier,
    message: String,
    onRetryClick: () -> Unit,
) {
    Column(
        modifier = modifier.padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            text = message,
            maxLines = 1,
            color = Color.Red,
        )
        OutlinedButton(onClick = onRetryClick) {
            Text(text = stringResource(R.string.try_again_string))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ErrorStatePreview() {
    ErrorState(
        modifier = Modifier,
        message = "Error",
        onRetryClick = {},
    )
}
