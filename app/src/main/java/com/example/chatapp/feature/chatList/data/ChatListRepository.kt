package com.example.chatapp.feature.chatList.data

import com.example.chatapp.feature.chat.di.MessageEntity
import com.example.chatapp.feature.chatList.data.model.UserListResponse
import com.example.chatapp.feature.chatList.domain.model.RoomEntity
import kotlinx.coroutines.flow.StateFlow

interface ChatListRepository {

    suspend fun observeRooms(stateFlow: (StateFlow<List<RoomEntity>?>) -> Unit)

    suspend fun getUsers(): UserListResponse

    suspend fun createChat(username: String)
}
