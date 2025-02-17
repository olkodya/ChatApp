package com.example.chatapp.feature.coinDetail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.feature.coinDetail.domain.GetCoinPriceHistoryUseCase
import com.example.chatapp.feature.coinDetail.domain.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    private val historyUseCase: GetCoinPriceHistoryUseCase,
) : ViewModel() {

    private val mutableCoinState: MutableStateFlow<CoinDetailScreenState> = MutableStateFlow(
        CoinDetailScreenState("", "", BigDecimal.ZERO, persistentListOf(), 0f)
    )
    val coinState: StateFlow<CoinDetailScreenState> = mutableCoinState.asStateFlow()

    private val mutableActions: Channel<CoinDetailEvent> = Channel()
    val action: Flow<CoinDetailEvent> = mutableActions.receiveAsFlow()

    fun handleAction(action: CoinDetailAction) {
        when (action) {
            is CoinDetailAction.OnStart -> setCoin(
                action.coinId, action.coinName, action.coinPrice
            )

            CoinDetailAction.OnBackClick -> navigateToCoinListScreen()
            is CoinDetailAction.OnRetryClick -> setCoin(
                action.coinId, action.coinName, action.coinPrice
            )

            is CoinDetailAction.OnChangePosition -> positionChanged(action.position)
        }
    }

    private fun positionChanged(position: Float) {
        mutableCoinState.value = coinState.value.copy(currentChartPosition = position)
    }

    private fun navigateToCoinListScreen() {
        viewModelScope.launch {
            mutableActions.send(CoinDetailEvent.GoBack)
        }
    }

    private fun setCoin(coinId: String, coinName: String, price: BigDecimal) {
        mutableCoinState.value = coinState.value.copy(
            coinId = coinId,
            coinName = coinName,
            currentPrice = price.setScale(5, RoundingMode.HALF_UP),
        )
        getCoinPriceHistory(coinId)
    }

    private fun getCoinPriceHistory(coinId: String) {
        viewModelScope.launch {
            try {
                mutableCoinState.value = coinState.value.copy(
                    loading = true, coinPriceHistory = persistentListOf(), errorMessage = null
                )
                val history = historyUseCase(coinId = coinId)

                mutableCoinState.value = coinState.value.copy(
                    coinPriceHistory = coinState.value.coinPriceHistory.toMutableList()
                        .apply { addAll(history.map { it.toState() }) }
                        .toImmutableList(),
                    loading = false,
                    errorMessage = null
                )

//                currentPriceUseCase(coinId).collect { price ->
//                    mutableCoinState.value = coinState.value.copy(currentPrice = price.priceUsd,
//                        coinPriceHistory = coinState.value.coinPriceHistory.toMutableList()
//                            .apply { add(price.toState(coinState.value.coinPriceHistory.size.toFloat())) }
//                            .toImmutableList(),
//                        loading = false,
//                        errorMessage = null,
//                        coinPriceIncreased = price.priceUsd > coinState.value.currentPrice)
//                }
            } catch (ex: Exception) {
                mutableCoinState.value = coinState.value.copy(
                    loading = false,
                    coinPriceHistory = persistentListOf(),
                    errorMessage = ex.message.toString()
                )
            }

        }
    }


    sealed class CoinDetailAction {
        data class OnStart(
            val coinId: String, val coinName: String, val coinPrice: BigDecimal
        ) : CoinDetailAction()

        data object OnBackClick : CoinDetailAction()
        data class OnRetryClick(
            val coinId: String, val coinName: String, val coinPrice: BigDecimal
        ) : CoinDetailAction()

        data class OnChangePosition(val position: Float) : CoinDetailAction()

    }

    sealed class CoinDetailEvent {
        data object GoBack : CoinDetailEvent()
    }
}
