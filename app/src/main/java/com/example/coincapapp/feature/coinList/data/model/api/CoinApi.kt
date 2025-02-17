package com.example.coincapapp.feature.coinList.data.model.api

import com.example.coincapapp.feature.coinDetail.data.model.CoinDetailListResponse
import com.example.coincapapp.feature.coinList.data.model.CoinListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinApi {
    @GET("assets")
    suspend fun loadPagingCoins(
        @Query("search") searchQuery: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): CoinListResponse
}