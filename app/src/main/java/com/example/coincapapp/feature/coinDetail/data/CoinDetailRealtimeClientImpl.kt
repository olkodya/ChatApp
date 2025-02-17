//package com.example.coincapapp.feature.coinDetail.data
//
//import com.example.coincapapp.feature.coinDetail.data.model.CoinDetailResponse
//import io.ktor.client.HttpClient
//import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
//import io.ktor.client.plugins.websocket.webSocketSession
//import io.ktor.client.request.url
//import io.ktor.websocket.Frame
//import io.ktor.websocket.close
//import io.ktor.websocket.readText
//import kotlinx.coroutines.CancellationException
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.consumeAsFlow
//import kotlinx.coroutines.flow.emitAll
//import kotlinx.coroutines.flow.filterIsInstance
//import kotlinx.coroutines.flow.flow
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.flow.mapNotNull
//import kotlinx.serialization.json.Json
//import java.math.BigDecimal
//import javax.inject.Inject
//
//class CoinDetailRealtimeClientImpl @Inject constructor(
//    private val client: HttpClient
//) : CoinDetailRealtimeClient {
//
//    private var session: DefaultClientWebSocketSession? = null
//
//    override suspend fun getCoinCurrentPrice(coinId: String): Flow<CoinDetailResponse> {
//        return flow {
//            session = client.webSocketSession {
//                url("wss://ws.coincap.io/prices?assets=$coinId")
//            }
//            val currentPrices = session!!
//                .incoming
//                .consumeAsFlow()
//                .filterIsInstance<Frame.Text>()
//                .mapNotNull { Json.decodeFromString<Map<String, String>>(it.readText()) }
//            try {
//                emitAll(currentPrices.map {
//                    CoinDetailResponse(
//                        priceUsd = it[coinId]?.toBigDecimal() ?: BigDecimal.ZERO,
//                        time = System.currentTimeMillis()
//                    )
//                })
//            } catch (e: CancellationException) {
//                session!!.close()
//            }
//
//        }
//    }
//
//
//}
