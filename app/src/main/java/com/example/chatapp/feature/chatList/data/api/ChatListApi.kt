package com.example.chatapp.feature.chatList.data.api

import com.example.chatapp.feature.chatList.data.model.RoomsResponse
import com.example.chatapp.feature.chatList.data.model.UserInfoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ChatListApi {

    @GET("/api/v1/rooms.get")
    suspend fun getRooms(): RoomsResponse

    @GET("/api/v1/users.info")
    suspend fun getUsers(@Query("userId") userId: String): UserInfoResponse

}

