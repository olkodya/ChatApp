package com.example.chatapp.feature.coinDetail.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import java.math.BigDecimal

@Composable
fun CoinDetailScreen(
    coinId: String,
    coinName: String,
    coinPrice: BigDecimal,
    routeBackStack: () -> Unit
) {
    val viewModel: CoinDetailViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        if (viewModel.coinState.value.coinPriceHistory.isEmpty())
            viewModel.handleAction(
                CoinDetailViewModel.CoinDetailAction.OnStart(
                    coinId = coinId,
                    coinName = coinName,
                    coinPrice = coinPrice,
                )
            )
        viewModel.action.collect { action ->
            when (action) {
                CoinDetailViewModel.CoinDetailEvent.GoBack -> routeBackStack()
            }
        }
    }

    CoinDetailContent(
        viewModel.coinState.collectAsState().value,
    ) { viewModel.handleAction(it) }
}
