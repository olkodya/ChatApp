package com.example.chatapp.feature.chatList.data.api

import com.example.chatapp.feature.chatList.data.model.UserInfoResponse
import com.example.chatapp.feature.chatList.data.model.UserListResponse
import com.example.chatapp.feature.chatList.data.model.CreateChatRequest
import com.example.chatapp.feature.chatList.data.model.CreateChatResponse
import com.example.chatapp.feature.chatList.data.model.RoomsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatListApi {

    @GET("/api/v1/rooms.get")
    suspend fun getRooms(): RoomsResponse

    @GET("/api/v1/users.info")
    suspend fun getUsersInfo(@Query("userId") userId: String): UserInfoResponse

    @GET("/api/v1/users.list")
    suspend fun getUsersList(@Query("count") count: Int): UserListResponse

    @POST("/api/v1/im.create")
    suspend fun createDM(@Body body: CreateChatRequest): CreateChatResponse
}
