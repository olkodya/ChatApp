package com.example.chatapp.feature.chat.data

import com.example.chatapp.feature.chat.di.MessageEntity
import com.example.chatapp.feature.chat.domain.model.ChatInfoEntity
import kotlinx.coroutines.flow.StateFlow

interface ChatRepository {

    suspend fun observeMessages(
        stateFlow: (StateFlow<List<MessageEntity>?>) -> Unit,
        roomId: String
    )

    suspend fun sendMessage(roomId: String, text: String?)

    suspend fun getRoomInfo(roomId: String): ChatInfoEntity
    suspend fun getUserInfo(userId: String): ChatInfoEntity
}