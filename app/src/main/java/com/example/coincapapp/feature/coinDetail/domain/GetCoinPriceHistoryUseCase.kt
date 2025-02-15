package com.example.coincapapp.feature.coinDetail.domain

interface GetCoinPriceHistoryUseCase {

    suspend operator fun invoke(coinId: String): List<CoinDetailEntity>
}
