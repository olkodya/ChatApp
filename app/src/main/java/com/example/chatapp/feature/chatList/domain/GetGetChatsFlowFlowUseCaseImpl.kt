package com.example.chatapp.feature.chatList.domain

import com.example.chatapp.feature.chatList.data.ChatListRepository
import com.example.chatapp.feature.chatList.domain.model.RoomEntity
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ObserveRoomsUseCaseImpl @Inject constructor(
    private val chatListRepository: ChatListRepository,
) : ObserveRoomsUseCase {

    override suspend fun invoke(stateFlow: (StateFlow<List<RoomEntity>?>) -> Unit) {
        return chatListRepository.observeRooms(stateFlow = stateFlow)
    }
}
