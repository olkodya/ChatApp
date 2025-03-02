package com.example.chatapp.feature.chat.data

import com.example.chatapp.feature.chat.data.model.PostMessageRequest
import com.example.chatapp.feature.chat.data.model.RoomInfoResponse
import com.example.chatapp.feature.chatList.data.model.UserInfoResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatApi {

    @POST("/api/v1/chat.sendMessage")
    suspend fun postMessage(@Body body: PostMessageRequest)

    @GET("/api/v1/rooms.info")
    suspend fun getRoomInfo(@Query("roomId") roomId: String): RoomInfoResponse

    @GET("/api/v1/users.info")
    suspend fun getUserInfo(@Query("userId") userId: String): UserInfoResponse

    @POST("/api/v1/chat.uploadMedia")
    suspend fun sendMedia(@Query("roomId") roomId: String, @Body data: MultipartBody.Part)
}
