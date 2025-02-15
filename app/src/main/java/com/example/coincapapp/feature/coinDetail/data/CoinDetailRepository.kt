package com.example.coincapapp.feature.coinDetail.data

import com.example.coincapapp.feature.coinDetail.data.model.CoinDetailListResponse

interface CoinDetailRepository {

    suspend fun getCoinPricesHistory(coinId: String): CoinDetailListResponse
}
