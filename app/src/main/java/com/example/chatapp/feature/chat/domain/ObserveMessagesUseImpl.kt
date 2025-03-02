package com.example.chatapp.feature.chat.domain

import com.example.chatapp.feature.chat.data.ChatRepository
import com.example.chatapp.feature.chat.di.MessageEntity
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ObserveMessagesUseImpl @Inject constructor(
    private val chatRepository: ChatRepository,
) : ObserveMessagesUse {

    override suspend fun invoke(
        roomId: String,
        stateFlow: (StateFlow<List<MessageEntity>?>) -> Unit
    ) = chatRepository.observeMessages(stateFlow, roomId)
}
