package com.example.coincapapp.feature.exchangeList.domain.entities

import com.example.coincapapp.feature.exchangeList.presentation.ExchangeState
import java.math.BigDecimal

data class ExchangeEntity(
    val id: String,
    val name: String,
    val rank: String,
    val percentTotalVolume: BigDecimal,
    val volumeUsd: BigDecimal,
    val tradingPairs: String,
    val socket: String,
    val exchangeUrl: String,
    val updated: String,
)

fun ExchangeEntity.toState() = ExchangeState(
    id = id,
    name = name,
    rank = rank,
    percentTotalVolume = percentTotalVolume,
    volumeUsd = volumeUsd,
    exchangeUrl = exchangeUrl
)
