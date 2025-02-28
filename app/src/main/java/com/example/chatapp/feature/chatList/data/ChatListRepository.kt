package com.example.chatapp.feature.chatList.data

import com.example.chatapp.feature.chatList.data.model.UserListResponse
import com.example.chatapp.feature.chatList.data.model.WebSocketMessage
import com.example.chatapp.feature.chatList.domain.model.RoomEntity
import kotlinx.coroutines.flow.StateFlow

interface ChatListRepository {

    suspend fun observeRooms(): StateFlow<List<RoomEntity>?>

    fun sendMessage(message: WebSocketMessage)

    suspend fun getUsers(): UserListResponse

    suspend fun createChat(username: String)
}
