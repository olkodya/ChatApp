package com.example.chatapp.feature.coinList.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import java.math.BigDecimal

@Composable
fun CoinListScreen(
    routeToCoinDetailScreen: (String, String, BigDecimal) -> Unit,
) {
    val viewModel: CoinListViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        viewModel.action.collect { action ->
            when (action) {
                is CoinListViewModel.CoinListEvent.NavigateToCoinDetail -> {
                    routeToCoinDetailScreen(action.coinId, action.coinName, action.price)
                }
            }
        }
    }

    CoinListContent(
        fieldState = viewModel.fieldState.collectAsState().value,
        coinsPagingState = viewModel.coinsPagingState.collectAsLazyPagingItems(),
        handleAction = { viewModel.handleAction(it) },
    )
}
