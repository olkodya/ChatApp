package com.example.chatapp.feature.coinDetail.data.model

import com.example.chatapp.feature.coinDetail.domain.CoinDetailEntity
import com.example.chatapp.utils.BigDecimalSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class CoinDetailResponse(
    @Serializable(with = BigDecimalSerializer::class)
    @SerialName("priceUsd")
    val priceUsd: BigDecimal = BigDecimal.ZERO,
    @SerialName("time")
    val time: Long = 0,
)

@Serializable
data class CoinDetailListResponse(
    @SerialName("data")
    val data: List<CoinDetailResponse> = emptyList()
)

fun CoinDetailResponse.toEntity(xValue: Int) =
    CoinDetailEntity(
        priceUsd = priceUsd,
        time = time,
        xValues = xValue
    )
