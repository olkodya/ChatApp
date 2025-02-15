package com.example.coincapapp.feature.exchangeList.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coincapapp.R
import com.example.coincapapp.components.ErrorState
import com.example.coincapapp.components.LoadingState
import kotlinx.collections.immutable.persistentListOf
import java.math.BigDecimal

@Composable
fun ExchangeListContent(
    exchangeListState: ExchangeListState,
    handleAction: (ExchangeListViewModel.ExchangeListAction) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        ExchangesTopAppBar()
        when (exchangeListState) {
            is ExchangeListState.Content -> ExchangeList(
                value = exchangeListState,
                handleAction = handleAction
            )

            ExchangeListState.Loading -> {
                LoadingState(Modifier.fillMaxSize())
            }

            is ExchangeListState.Error -> {
                ErrorState(
                    modifier = Modifier.fillMaxSize(),
                    message = exchangeListState.message ?: stringResource(R.string.error_string),
                    onRetryClick = {
                        handleAction(ExchangeListViewModel.ExchangeListAction.OnRetryClick)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangesTopAppBar() {
    TopAppBar(title = { Text(text = "Exchanges List") })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeList(
    value: ExchangeListState,
    handleAction: (ExchangeListViewModel.ExchangeListAction) -> Unit
) {
    if (value is ExchangeListState.Content) {
        PullToRefreshBox(
            isRefreshing = value.isRefreshing,
            onRefresh = { handleAction(ExchangeListViewModel.ExchangeListAction.OnSwipeToRefresh) },
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(
                    count = value.exchanges.size,
                    key = { id -> value.exchanges[id].id }) { index ->
                    val exchange = value.exchanges[index]
                    ExchangeCard(exchange = exchange, handleAction = handleAction)
                }
            }
        }
    }
}

@Composable
fun ExchangeCard(
    exchange: ExchangeState,
    handleAction: (ExchangeListViewModel.ExchangeListAction) -> Unit
) {
    Card(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp)
            .clickable {
                handleAction(
                    ExchangeListViewModel.ExchangeListAction.OnExchangeUrlClicked(
                        exchange.exchangeUrl
                    )
                )
            }
    ) {
        Column(Modifier.padding(16.dp)) {
            Row {
                Text(
                    modifier = Modifier.padding(end = 8.dp),
                    text = stringResource(R.string.hashtag).format(exchange.rank),
                    fontSize = 16.sp
                )
                Text(
                    text = exchange.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                )
            }
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(R.string.volumeUsd).format(
                    exchange.volumeUsd,
                    stringResource(R.string.usd)
                ),
                fontSize = 16.sp,
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = stringResource(R.string.percentTotalVolume).format(
                    exchange.percentTotalVolume,
                    stringResource(R.string.percent)
                ),
                fontSize = 16.sp
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ExchangeCardPreview() {
    ExchangeCard(
        exchange = ExchangeState(
            id = "binance",
            name = "Binance",
            rank = "1",
            percentTotalVolume = BigDecimal("37.95875"),
            volumeUsd = BigDecimal("15766846950.38742"),
            exchangeUrl = ""
        )
    ) {}
}

@Composable
@Preview(showBackground = true)
fun ExchangeListPreview() {
    ExchangeListContent(
        exchangeListState = ExchangeListState.Content(
            exchanges = persistentListOf(
                ExchangeState(
                    id = "binance",
                    name = "Binance",
                    rank = "1",
                    percentTotalVolume = BigDecimal("37.95875"),
                    volumeUsd = BigDecimal("15766846950.38742"),
                    exchangeUrl = ""
                ),
                ExchangeState(
                    id = "crypto_com",
                    name = "Crypto.com",
                    rank = "2",
                    percentTotalVolume = BigDecimal("37.95875"),
                    volumeUsd = BigDecimal("15766846950.38742"),
                    exchangeUrl = ""
                ),
                ExchangeState(
                    id = "white_bit",
                    name = "WhiteBit",
                    rank = "1",
                    percentTotalVolume = BigDecimal("37.95875"),
                    volumeUsd = BigDecimal("15766846950.38742"),
                    exchangeUrl = ""
                )
            )
        )
    ) {

    }
}
