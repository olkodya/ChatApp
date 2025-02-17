package com.example.chatapp.feature.exchangeList.data.api

import com.example.chatapp.feature.exchangeList.data.model.ExchangeListResponse
import retrofit2.http.GET

interface ExchangeApi {
    @GET("exchanges")
    suspend fun loadExchanges(): ExchangeListResponse
}