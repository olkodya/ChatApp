package com.example.coincapapp.feature.exchangeList.data

import com.example.coincapapp.feature.exchangeList.data.model.ExchangeListResponse

interface ExchangeRepository {

    suspend fun loadExchanges(): ExchangeListResponse
}