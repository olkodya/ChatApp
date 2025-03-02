package com.example.chatapp.data

import android.util.Log
import com.example.chatapp.BuildConfig
import com.example.chatapp.feature.authorization.data.AuthPreferences
import com.example.chatapp.feature.chatList.data.model.WebSocketMessage
import com.example.chatapp.feature.chatList.data.model.toWebSocketMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class WebSocketDataStoreImpl(
    @Named("WebSocketOkHttpClient")
    private val okHttpClient: OkHttpClient,
    private val authPreferences: AuthPreferences,
    private val json: Json,
) : WebSocketDataStore {

    private val mutableResponseFlow = MutableStateFlow<String?>(null)
    override val responseFlow: StateFlow<String?> = mutableResponseFlow

    private lateinit var webSocket: WebSocket
    private var shouldReconnect = true
    private val webSocketListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {

        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            if (text.contains("ping")) {
                sendMessage(WebSocketMessage.Pong())
                return
            }
            mutableResponseFlow.value = text
//            val responseId: String? = (Json.parseToJsonElement(text) as JsonObject)["id"]
//                ?.jsonPrimitive?.content
            Log.d("WebSocketDataStoreImpl onMessage", "")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {

        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            if (shouldReconnect) reconnect()
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            if (shouldReconnect) reconnect()
        }
    }

    init {
        connect()
    }

    override fun sendMessage(message: WebSocketMessage) {
        val jsonString: String = json.encodeToString(message)
        if (::webSocket.isInitialized.not()) {
            connect()
        }
        webSocket.send(jsonString)
    }

    private fun connect(): Boolean {
        val authData = authPreferences.getAuthData() ?: return false
        initWebSocket()
        shouldReconnect = true
        sendMessage(WebSocketMessage.Connect())
        sendMessage(authData.toWebSocketMessage())
        return true
    }

    private fun initWebSocket() {
        val request = Request.Builder().url(url = BuildConfig.BASE_SOCKET_URL).build()
        webSocket = okHttpClient.newWebSocket(request, webSocketListener)
        okHttpClient.dispatcher.executorService.shutdown()
    }

    private fun reconnect() {
        initWebSocket()
    }
}
