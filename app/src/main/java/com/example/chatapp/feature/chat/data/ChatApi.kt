package com.example.chatapp.feature.chat.data

import com.example.chatapp.feature.chat.data.model.MessageResponse
import com.example.chatapp.feature.chat.data.model.PostMessageRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatApi {

    @POST("/api/v1/chat.sendMessage")
    suspend fun postMessage(@Body body: PostMessageRequest)

}