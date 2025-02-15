package com.example.coincapapp.feature.coinList.domain

import androidx.paging.PagingData
import com.example.coincapapp.feature.coinList.presentation.CoinState
import kotlinx.coroutines.flow.Flow

interface GetCoinListUseCase {

    suspend operator fun invoke(searchQuery: String): Flow<PagingData<CoinState>>
}
