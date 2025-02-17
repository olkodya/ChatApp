package com.example.chatapp.feature.coinList.domain.entities

import java.math.BigDecimal

data class CoinEntity(
    val id: String,
    val rank: String,
    val symbol: String,
    val name: String,
    val supply: String,
    val maxSupply: String,
    val marketCapUsd: String,
    val volumeUsd24Hr: String,
    val priceUsd: BigDecimal,
    val changePercent24Hr: BigDecimal,
    val vwap24Hr: String,
    val explorer: String,
)
