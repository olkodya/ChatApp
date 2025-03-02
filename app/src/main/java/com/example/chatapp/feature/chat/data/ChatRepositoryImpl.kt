package com.example.chatapp.feature.chatList.data

import android.content.Context
import com.example.chatapp.data.WebSocketDataStore
import com.example.chatapp.feature.authorization.data.AuthData
import com.example.chatapp.feature.authorization.data.AuthPreferences
import com.example.chatapp.feature.chat.data.ChatApi
import com.example.chatapp.feature.chat.data.ChatRepository
import com.example.chatapp.feature.chat.data.model.MessageResponse
import com.example.chatapp.feature.chat.data.model.MessagesCallResponse
import com.example.chatapp.feature.chat.data.model.MessagesSubResponse
import com.example.chatapp.feature.chat.data.model.PostMessageRequest
import com.example.chatapp.feature.chat.data.model.TextMessage
import com.example.chatapp.feature.chat.di.MessageEntity
import com.example.chatapp.feature.chat.di.toEntity
import com.example.chatapp.feature.chat.domain.model.ChatInfoEntity
import com.example.chatapp.feature.chat.domain.model.toEntity
import com.example.chatapp.feature.chatList.data.model.WebSocketMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.OkHttpClient
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named

class ChatRepositoryImpl @Inject constructor(
    private val webSocketDataStore: WebSocketDataStore,
    private val api: ChatApi,
    @Named("WebSocketOkHttpClient") private val okHttpClient: OkHttpClient,
    private val authPreferences: AuthPreferences,
    private val formattedJson: Json,
    private val context: Context
) : ChatRepository {

    private lateinit var authData: AuthData


    private val messagesMutableStateFlow = MutableStateFlow<List<MessageEntity>?>(null)
    val messagesStateFlow: StateFlow<List<MessageEntity>?> = messagesMutableStateFlow.asStateFlow()

    private fun callsProcessing(text: String, responseId: String) = when (responseId) {
        MASSAGES_CALL_ID -> {
            try {
                val messagesResponse: List<MessageResponse>? =
                    formattedJson.decodeFromString<MessagesCallResponse>(text)
                        .result
                        ?.messages
                val messagesEntity: List<MessageEntity>? = messagesResponse
                    ?.map { it.toEntity(authData.userId) }
                messagesMutableStateFlow.value = messagesEntity
            } catch (e: Exception) {
            }
        }

        else -> Unit
    }

    private fun subscriptionsProcessing(text: String, eventName: String) = when {

        eventName.contains(SUBSCRIPTIONS_STREAM_ROOM_MESSAGE) -> {
            messagesChangedProcessing(text = text)
        }

        else -> Unit
    }

    private fun messagesChangedProcessing(text: String) {
        try {
            val messagesResponse: List<MessageResponse> =
                formattedJson.decodeFromString<MessagesSubResponse>(text)
                    .fields.messages ?: return
            val messagesEntity: List<MessageEntity>? = messagesResponse
                .map { it.toEntity(authData.userId) }
            messagesMutableStateFlow.update { currentMessages ->
                val updatedMessages =
                    (messagesEntity ?: emptyList()) + (currentMessages ?: emptyList())
                updatedMessages
            }
        } catch (e: Exception) {
            print("")
        }
    }


    fun onTextSocketProcessing(text: String) {
        try {
            val responseId: String? = (Json.parseToJsonElement(text) as JsonObject)["id"]
                ?.jsonPrimitive?.content

            val eventName: String? = Json.parseToJsonElement(text)
                .jsonObject["collection"]?.jsonPrimitive?.content
            when {
                eventName != null -> subscriptionsProcessing(text = text, eventName = eventName)

                responseId != null -> callsProcessing(
                    text = text,
                    responseId = responseId
                )
            }
        } catch (exception: Exception) {
            print(exception)
        }
    }

    override suspend fun observeMessages(
        stateFlow: (StateFlow<List<MessageEntity>?>) -> Unit,
        roomId: String
    ) {
        authData = authPreferences.getAuthData()
            ?: return throw IOException("Auth data not set")

        webSocketDataStore.sendMessage(
            WebSocketMessage.Unsubscribe(MESSAGES_SUB_ID)
        )
        webSocketDataStore.sendMessage(
            WebSocketMessage.LoadHistoryRequest.create(
                id = MASSAGES_CALL_ID,
                roomId = roomId,
                limit = GET_MASSAGES_LIMIT
            )
        )
        webSocketDataStore.sendMessage(
            WebSocketMessage.MessagesSubscribe.messagesFactory(
                id = MESSAGES_SUB_ID,
                roomId = roomId,
            )
        )
        stateFlow(messagesStateFlow)
        webSocketDataStore.responseFlow.drop(1).collect { text ->
            if (text == null) return@collect
            onTextSocketProcessing(text)
        }
    }

    override suspend fun sendMessage(roomId: String, text: String?) = api.postMessage(
        body = PostMessageRequest(message = TextMessage(rid = roomId, msg = text))
    )

    override suspend fun getRoomInfo(roomId: String): ChatInfoEntity {
        authData = authPreferences.getAuthData() ?: return throw IOException("Auth data not set")
        return api.getRoomInfo(roomId = roomId).toEntity(authData.username, authData.userId)
    }

    override suspend fun getUserInfo(userId: String): ChatInfoEntity {
        val user = api.getUserInfo(userId = userId)
        return ChatInfoEntity(
            username = user.user.name,
            chatType = "d",
            userId = user.user.id,
            chatName = user.user.name,
            chatAvatarUrl = ""
        )
    }

    private companion object {
        const val MASSAGES_CALL_ID = "477ca7f6-d8e9-4921-a407-b32da33e1b9a"
        const val GET_MASSAGES_LIMIT = 500
        const val MESSAGES_SUB_ID = "f38bd715-97bf-48fd-b27a-12b825133f85"
        const val SUBSCRIPTIONS_STREAM_ROOM_MESSAGE = "stream-room-messages"
    }
}
