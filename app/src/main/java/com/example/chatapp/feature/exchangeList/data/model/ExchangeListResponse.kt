package com.example.chatapp.feature.exchangeList.data.model

import com.example.chatapp.feature.exchangeList.domain.entities.ExchangeEntity
import com.example.chatapp.utils.BigDecimalSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class ExchangeListResponse(
    val data: List<ExchangeResponse>
)

@Serializable
data class ExchangeResponse(
    @SerialName("exchangeId")
    val id: String? = "",
    @SerialName("name")
    val name: String? = "",
    @SerialName("rank")
    val rank: String? = "",
    @Serializable(with = BigDecimalSerializer::class)
    @SerialName("percentTotalVolume")
    val percentTotalVolume: BigDecimal? = BigDecimal.ZERO,
    @SerialName("volumeUsd")
    @Serializable(with = BigDecimalSerializer::class)
    val volumeUsd: BigDecimal? = BigDecimal.ZERO,
    @SerialName("tradingPairs")
    val tradingPairs: String? = "",
    @SerialName("socket")
    val socket: String?="",
    @SerialName("exchangeUrl")
    val exchangeUrl: String?="",
    @SerialName("updated")
    val updated: String?="",
)

fun ExchangeResponse.toEntity() = ExchangeEntity(
    id = requireNotNull(id),
    name = requireNotNull(name),
    rank = requireNotNull(rank ?: ""),
    percentTotalVolume = requireNotNull(percentTotalVolume ?: BigDecimal.ZERO),
    volumeUsd = requireNotNull(volumeUsd ?: BigDecimal.ZERO),
    tradingPairs = requireNotNull(tradingPairs ?: ""),
    socket = requireNotNull(socket ?: ""),
    exchangeUrl = requireNotNull(exchangeUrl ?: ""),
    updated = requireNotNull(updated ?: "")
)
