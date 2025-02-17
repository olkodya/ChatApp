package com.example.chatapp.feature.exchangeList.presentation

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ExchangeListScreen() {
    val viewModel: ExchangeListViewModel = hiltViewModel()
    val ctx = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.action.collect { action ->
            when (action) {
                is ExchangeListViewModel.ExchangeListEvent.OpenUrlInBrowser -> {
                    val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(action.exchangeUrl))
                    ctx.startActivity(urlIntent)
                }
            }
        }
    }


    ExchangeListContent(
        exchangeListState = viewModel.exchangeListState.collectAsState().value,
        handleAction = { viewModel.handleAction(it) },
    )
}
