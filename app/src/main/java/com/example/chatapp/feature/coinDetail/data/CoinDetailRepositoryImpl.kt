package com.example.chatapp.feature.coinDetail.data

import com.example.chatapp.feature.coinDetail.data.api.CoinDetailApi
import com.example.chatapp.feature.coinDetail.data.model.CoinDetailListResponse
import javax.inject.Inject

class CoinDetailRepositoryImpl @Inject constructor(
    private val api: CoinDetailApi
) : CoinDetailRepository {

    override suspend fun getCoinPricesHistory(coinId: String): CoinDetailListResponse =
        api.getCoinPricesHistory(coinId)
}
