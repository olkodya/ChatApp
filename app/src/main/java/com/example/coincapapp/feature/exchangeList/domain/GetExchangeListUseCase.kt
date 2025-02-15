package com.example.coincapapp.feature.exchangeList.domain

import com.example.coincapapp.feature.exchangeList.domain.entities.ExchangeEntity

interface GetExchangeListUseCase {

    suspend operator fun invoke(): List<ExchangeEntity>
}
