package com.example.chatapp.feature.coinList.domain

import androidx.paging.PagingData
import com.example.chatapp.feature.coinList.presentation.CoinState
import kotlinx.coroutines.flow.Flow

interface GetCoinListUseCase {

    suspend operator fun invoke(searchQuery: String): Flow<PagingData<CoinState>>
}
