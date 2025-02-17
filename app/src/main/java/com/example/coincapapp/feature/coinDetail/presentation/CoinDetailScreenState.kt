package com.example.coincapapp.feature.coinDetail.presentation

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.math.BigDecimal

@Immutable
data class CoinDetailScreenState(
    val coinId: String,
    val coinName: String,
    val currentPrice: BigDecimal,
    val coinPriceHistory: ImmutableList<CoinDetailState> = persistentListOf(),
    val currentChartPosition: Float,
    val loading: Boolean = false,
    val errorMessage: String? = null,
    val coinPriceIncreased: Boolean = true
) {
    val isLoading: Boolean
        get() = loading
    val hasError: String?
        get() = errorMessage
}

@Immutable
data class CoinDetailState(
    val time: String,
//    val data: Entry,
)
