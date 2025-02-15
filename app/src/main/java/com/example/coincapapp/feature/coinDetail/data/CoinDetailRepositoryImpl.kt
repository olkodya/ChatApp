package com.example.coincapapp.feature.coinDetail.data

import com.example.coincapapp.feature.coinDetail.data.model.CoinDetailListResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

class CoinDetailRepositoryImpl @Inject constructor(
    private val client: HttpClient
) : CoinDetailRepository {

    override suspend fun getCoinPricesHistory(coinId: String): CoinDetailListResponse =
        client.get("assets/${coinId}/history")
        {
            parameter("interval", "m1")
        }
            .body()
}
