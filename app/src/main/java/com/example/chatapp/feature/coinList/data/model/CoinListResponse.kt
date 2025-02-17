package com.example.chatapp.feature.coinList.data.model

import com.example.chatapp.feature.coinList.domain.entities.CoinEntity
import com.example.chatapp.utils.BigDecimalSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class CoinListResponse(
    val data: List<CoinResponse>
)

@Serializable
data class CoinResponse(
    @SerialName("id")
    val id: String?,
    @SerialName("rank")
    val rank: String?,
    @SerialName("symbol")
    val symbol: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("supply")
    val supply: String?,
    @SerialName("maxSupply")
    val maxSupply: String?,
    @SerialName("marketCapUsd")
    val marketCapUsd: String?,
    @SerialName("volumeUsd24Hr")
    val volumeUsd24Hr: String?,
    @SerialName("priceUsd")
    @Serializable(with = BigDecimalSerializer::class)
    val priceUsd: BigDecimal?,
    @Serializable(with = BigDecimalSerializer::class)
    @SerialName("changePercent24Hr")
    val changePercent24Hr: BigDecimal?,
    @SerialName("vwap24Hr")
    val vwap24Hr: String?,
    @SerialName("explorer")
    val explorer: String? = "",
)

fun CoinResponse.toEntity() = CoinEntity(
    id = requireNotNull(id ?: ""),
    rank = requireNotNull(rank ?: ""),
    symbol = requireNotNull(symbol ?: ""),
    name = requireNotNull(name ?: ""),
    supply = requireNotNull(supply ?: ""),
    maxSupply = requireNotNull(maxSupply ?: ""),
    marketCapUsd = requireNotNull(marketCapUsd ?: ""),
    volumeUsd24Hr = requireNotNull(volumeUsd24Hr ?: ""),
    priceUsd = requireNotNull(priceUsd ?: BigDecimal.ZERO),
    changePercent24Hr = requireNotNull(changePercent24Hr ?: BigDecimal.ZERO),
    vwap24Hr = requireNotNull(vwap24Hr ?: ""),
    explorer = requireNotNull(explorer ?: ""),
)
