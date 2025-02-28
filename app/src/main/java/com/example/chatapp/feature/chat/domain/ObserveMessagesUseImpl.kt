package com.example.chatapp.feature.chat.domain

import com.example.chatapp.feature.chat.di.MessageEntity
import com.example.chatapp.feature.chatList.data.ChatListRepository
import com.example.chatapp.feature.chatList.domain.model.RoomEntity
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ObserveMessagesUseImpl @Inject constructor(
    private val chatListRepository: ChatListRepository,
) : ObserveMessagesUse {

    override suspend fun invoke(roomId: String): StateFlow<List<MessageEntity>?> {
        return chatListRepository.observeMessages(roomId = roomId)
    }
}