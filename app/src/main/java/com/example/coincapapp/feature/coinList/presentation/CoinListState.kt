package com.example.coincapapp.feature.coinList.presentation

import androidx.compose.runtime.Immutable
import com.example.coincapapp.feature.coinList.domain.entities.CoinEntity
import java.math.BigDecimal


@Immutable
data class CoinState(
    val id: String,
    val rank: String,
    val symbol: String,
    val name: String,
    val priceUsd: BigDecimal,
    val changePercent24Hr: BigDecimal,
    val isPercentPositive: Boolean,
)

fun CoinEntity.toState() = CoinState(
    id = id,
    rank = rank,
    symbol = symbol,
    name = name,
    priceUsd = priceUsd,
    changePercent24Hr = changePercent24Hr,
    isPercentPositive = changePercent24Hr >= BigDecimal.ZERO
)
