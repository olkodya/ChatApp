package com.example.chatapp.feature.chatList.data.api

import com.example.chatapp.feature.chatList.data.model.RoomsResponse
import retrofit2.http.GET

interface ChatListApi {

    @GET("/api/v1/rooms.get")
    suspend fun getRooms(): RoomsResponse

//    @GET("/api/v1/users.list")
//    suspend fun getUsers(): UserResponse

}

