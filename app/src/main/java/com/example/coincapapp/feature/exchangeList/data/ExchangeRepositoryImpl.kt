package com.example.coincapapp.feature.exchangeList.data

import com.example.coincapapp.feature.exchangeList.data.model.ExchangeListResponse
import com.example.coincapapp.feature.exchangeList.data.api.ExchangeApi
import javax.inject.Inject

class ExchangeRepositoryImpl @Inject constructor(
    private val api: ExchangeApi
) : ExchangeRepository {

    override suspend fun loadExchanges(): ExchangeListResponse = api.loadExchanges()
}
