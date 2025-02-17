package com.example.coincapapp.feature.coinDetail.data

import com.example.coincapapp.feature.coinDetail.data.api.CoinDetailApi
import com.example.coincapapp.feature.coinDetail.data.model.CoinDetailListResponse
import javax.inject.Inject

class CoinDetailRepositoryImpl @Inject constructor(
    private val api: CoinDetailApi
) : CoinDetailRepository {

    override suspend fun getCoinPricesHistory(coinId: String): CoinDetailListResponse =
        api.getCoinPricesHistory(coinId)
}
