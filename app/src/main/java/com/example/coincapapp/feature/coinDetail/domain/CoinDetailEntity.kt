package com.example.coincapapp.feature.coinDetail.domain

import com.example.coincapapp.feature.coinDetail.presentation.CoinDetailState
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class CoinDetailEntity(
    val priceUsd: BigDecimal,
    val time: Long,
    val xValues: Int,
)

fun CoinDetailEntity.toState(xValue: Float = xValues.toFloat()): CoinDetailState {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = time
    val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val formattedDate = formatter.format(calendar.time)
    return CoinDetailState(
//        data = Entry(xValue, priceUsd.toFloat()),
        time = formattedDate
    )
}