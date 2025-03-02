package com.example.chatapp.feature.chat.data

import com.example.chatapp.feature.chat.di.MessageEntity
import kotlinx.coroutines.flow.StateFlow

interface ChatRepository {

    suspend fun observeMessages(
        stateFlow: (StateFlow<List<MessageEntity>?>) -> Unit,
        roomId: String
    )

    suspend fun sendMessage(roomId: String, text: String?)
}