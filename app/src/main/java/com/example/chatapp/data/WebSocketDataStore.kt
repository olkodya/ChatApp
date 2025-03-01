package com.example.chatapp.data

import com.example.chatapp.feature.chatList.data.model.WebSocketMessage
import kotlinx.coroutines.flow.StateFlow

interface WebSocketDataStore {

    val responseFlow: StateFlow<String?>

    fun sendMessage(message: WebSocketMessage)
}