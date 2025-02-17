package com.example.chatapp.feature.coinDetail.data

import com.example.chatapp.feature.coinDetail.data.model.CoinDetailListResponse

interface CoinDetailRepository {

    suspend fun getCoinPricesHistory(coinId: String): CoinDetailListResponse
}
