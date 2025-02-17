package com.example.chatapp.feature.coinDetail.data.api

import com.example.chatapp.feature.coinDetail.data.model.CoinDetailListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinDetailApi {
    @GET("assets/{coinId}/history")
    suspend fun getCoinPricesHistory(
        @Path("coinId") coinId: String,
        @Query("interval") interval: String = "m1"
    ): CoinDetailListResponse
}
