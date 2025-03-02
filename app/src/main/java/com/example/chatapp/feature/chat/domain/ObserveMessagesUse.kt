package com.example.chatapp.feature.chat.domain

import com.example.chatapp.feature.chat.di.MessageEntity
import kotlinx.coroutines.flow.StateFlow

interface ObserveMessagesUse {

    suspend operator fun invoke(
        roomId: String,
        stateFlow: (StateFlow<List<MessageEntity>?>) -> Unit,
    )
}
