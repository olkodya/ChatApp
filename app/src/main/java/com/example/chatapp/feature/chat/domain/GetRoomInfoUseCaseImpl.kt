package com.example.chatapp.feature.chat.domain

import android.util.Log
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatapp.feature.chat.data.ChatRepository
import com.example.chatapp.feature.chat.domain.model.ChatInfoEntity
import jakarta.inject.Inject

class GetRoomInfoUseCaseImpl @Inject constructor(private val chatRepository: ChatRepository) :
    GetRoomInfoUseCase {
    override suspend fun invoke(roomId: String): ChatInfoEntity {
        var chat = chatRepository.getRoomInfo(roomId = roomId)
        Log.d("chatasxcsam,csdcm,psa1", chat.toString())
        if (chat.chatType != "c") {
            runCatching {
               return chatRepository.getUserInfo(userId = chat.userId?:"")

            }.onFailure{
                print(it)
            }
        }
        Log.d("chatasxcsam,csdcm,psa", chat.toString())
        return chat
    }
}