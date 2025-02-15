package com.example.coincapapp.feature.exchangeList.domain

import com.example.coincapapp.feature.exchangeList.data.ExchangeRepository
import com.example.coincapapp.feature.exchangeList.data.model.toEntity
import com.example.coincapapp.feature.exchangeList.domain.entities.ExchangeEntity
import javax.inject.Inject

class GetExchangeListUseCaseImpl @Inject constructor(
    private val repository: ExchangeRepository,
) : GetExchangeListUseCase {

    override suspend fun invoke(): List<ExchangeEntity> {
        val entities = repository.loadExchanges().data.map {
            it.toEntity()
        }
        return entities
    }
}
