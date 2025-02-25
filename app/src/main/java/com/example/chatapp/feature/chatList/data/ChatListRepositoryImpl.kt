package com.example.chatapp.feature.chatList.data

import android.util.Log
import com.example.chatapp.feature.authorization.data.AuthData
import com.example.chatapp.feature.authorization.data.AuthPreferences
import com.example.chatapp.feature.chatList.data.api.ChatListApi
import com.example.chatapp.feature.chatList.data.model.RoomResponse
import com.example.chatapp.feature.chatList.data.model.RoomsResponse
import com.example.chatapp.feature.chatList.data.model.RoomsResponseSubscription
import com.example.chatapp.feature.chatList.data.model.SubscriptionResponse
import com.example.chatapp.feature.chatList.data.model.SubscriptionsResponse
import com.example.chatapp.feature.chatList.data.model.SubscriptionsSubscriptionResponse
import com.example.chatapp.feature.chatList.data.model.WebSocketMessage
import com.example.chatapp.feature.chatList.data.model.toWebSocketMessage
import com.example.chatapp.feature.chatList.domain.model.RoomEntity
import com.example.chatapp.feature.chatList.domain.model.toEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.IOException
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ChatListRepositoryImpl @Inject constructor(
    private val api: ChatListApi,
    @Named("WebSocketOkHttpClient") private val okHttpClient: OkHttpClient,
    private val authPreferences: AuthPreferences,
) : ChatListRepository {

    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private lateinit var webSocket: WebSocket
//    private lateinit var authData: AuthData
    private var shouldReconnect = true
    private val formattedJson = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        classDiscriminatorMode = ClassDiscriminatorMode.NONE
    }

    private val roomsMutableStateFlow = MutableStateFlow<List<RoomEntity>?>(null)
    val roomsStateFlow: StateFlow<List<RoomEntity>?> = roomsMutableStateFlow.asStateFlow()
    
    private fun checkAndLoadConversationalistInfo() {
//        roomsStateFlow.value?.forEach { room ->
//            repositoryScope.launch {
//                room.userId?.let { userId ->
//                    api.getUsers(userId)
//                }
//            }
//        }

            roomsStateFlow.value?.forEach { room ->
                room.userId?.let { userId ->
                    repositoryScope.launch {
                        try {
                            val response = api.getUsers(userId)
                            // Обновляем StateFlow сразу после получения каждого ответа
                            roomsMutableStateFlow.value = roomsMutableStateFlow.value?.map { currentRoom ->
                                if (currentRoom.id == room.id) {
                                    currentRoom.copy(
                                        name = response.user.name,
                                        userName = response.user.username,
                                    )
                                } else {
                                    currentRoom
                                }
                            }
                        } catch (e: Exception) {
                            println("Ошибка при загрузке пользователя $userId: ${e.message}")
                        }
                    }
                }
            }
    }

    override suspend fun observeRooms(): StateFlow<List<RoomEntity>?> {
        val authData = authPreferences.getAuthData()
            ?: return throw IOException("Auth data not set")
        if (::webSocket.isInitialized.not()) {
            connect()
        }
        sendMessage(WebSocketMessage.RoomsGet(id = ROOMS_CALL_ID))
        sendMessage(WebSocketMessage.SubscriptionsGet(id = SUBSCRIPTIONS_CALL_ID))
        sendMessage(
            WebSocketMessage.RoomsSubscribe.roomsFactory(
                id = ROOMS_SUB_ID,
                userId = authData.userId,
            )
        )
        sendMessage(
            WebSocketMessage.SubscriptionsSubscribe.subscriptionsFactory(
                id = SUBSCRIPTIONS_SUB_ID,
                userId = authData.userId,
            )
        )
        return roomsStateFlow
    }

    private suspend fun connect(): Boolean {
        val authData = authPreferences.getAuthData() ?: return false
        initWebSocket()
        shouldReconnect = true
        sendMessage(WebSocketMessage.Connect())
        sendMessage(authData.toWebSocketMessage())
        return true
    }

    fun reconnect() {
        Log.e("socketCheck", "reconnect()")
        initWebSocket()
    }

    private fun initWebSocket() {
        val request = Request.Builder().url(url = BASE_URL).build()
        webSocket = okHttpClient.newWebSocket(request, webSocketListener)
        okHttpClient.dispatcher.executorService.shutdown()
    }

    override fun sendMessage(message: WebSocketMessage) {
        runCatching {
            val jsonString: String = formattedJson.encodeToString(message)
            if (::webSocket.isInitialized) webSocket.send(jsonString)
        }.onFailure {
            print(it)
        }
    }

    fun disconnect() {
        if (::webSocket.isInitialized) webSocket.close(1000, "Do not need connection anymore.")
        shouldReconnect = false
    }

    private fun callsProcessing(text: String, responseId: String) = when (responseId) {
        ROOMS_CALL_ID -> {
            val entities: List<RoomEntity> =
                formattedJson.decodeFromString(
                    RoomsResponse.serializer(),
                    text
                ).result.update.map { it.toEntity(0, "sf") }
            roomsMutableStateFlow.value = entities
            checkAndLoadConversationalistInfo()
        }

        SUBSCRIPTIONS_CALL_ID -> {
            val subscriptions: SubscriptionsResponse =
                formattedJson.decodeFromString(SubscriptionsResponse.serializer(), text)
            Log.d("d", subscriptions.toString())
            subscriptions.result.update.meagreSubscriptionsToRoomsStateFlow()
        }

        else -> Unit
    }


    private fun subscriptionsProcessing(text: String, eventName: String) = when {
        eventName.contains(ROOMS_CHANGED_EVENT_NAME) -> {
            roomsChangedProcessing(text = text)
            checkAndLoadConversationalistInfo()
        }

        eventName.contains(SUBSCRIPTIONS_CHANGED_EVENT_NAME) -> {
            subscriptionsChangedProcessing(text = text)
        }

        else -> Unit
    }


    private fun roomsChangedProcessing(text: String) {
        val roomsResponseSubscription: RoomsResponseSubscription =
            formattedJson.decodeFromString(
                RoomsResponseSubscription.serializer(),
                text
            )

        val updatedRooms: List<RoomEntity> =
            roomsResponseSubscription.fields.args.mapNotNull { arg ->
                try {
                    formattedJson.decodeFromJsonElement(
                        deserializer = RoomResponse.serializer(),
                        element = arg
                    ).toEntity(0, "")
                } catch (_: Exception) {
                    null
                }
            }

        roomsMutableStateFlow.update { currentRooms ->
            val roomsMap = currentRooms?.associateBy { it.id }?.toMutableMap()
            updatedRooms.forEach { room ->
                roomsMap?.set(room.id, room)
            }
            roomsMap?.values?.toList()
        }
    }


    fun subscriptionsChangedProcessing(text: String) {
        val subscriptionsSubscriptionResponse: SubscriptionsSubscriptionResponse =
            formattedJson.decodeFromString(
                SubscriptionsSubscriptionResponse.serializer(),
                text
            )
        val subscriptionsUpdates: List<SubscriptionResponse> =
            subscriptionsSubscriptionResponse.fields.args.mapNotNull { arg ->
                try {
                    formattedJson.decodeFromJsonElement(
                        deserializer = SubscriptionResponse.serializer(),
                        element = arg
                    )
                } catch (_: Exception) {
                    null
                }
            }
        Log.d("d", subscriptionsUpdates.toString())
        subscriptionsUpdates.meagreSubscriptionsToRoomsStateFlow()
    }

    fun List<SubscriptionResponse>.meagreSubscriptionsToRoomsStateFlow() {
        roomsMutableStateFlow.update { currentRooms ->
            val roomsMap = currentRooms?.associateBy { it.id }?.toMutableMap()
            forEach { subscription ->
                roomsMap?.get(subscription.rid)?.let { room ->
                    roomsMap[subscription.rid] = room.copy(
                        unreadMessagesNumber = subscription.unread
                    )
                }
            }
            roomsMap?.values?.toList()
        }
    }

    private val webSocketListener = object : WebSocketListener() {
        //called when connection succeeded
        //we are sending a message just after the socket is opened
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.e("socketCheck", "onOpen()")
        }

        //called when text message received
        override fun onMessage(webSocket: WebSocket, text: String) {
            try {
                if (text.contains("ping")) {
                    sendMessage(WebSocketMessage.Pong())
                    return
                }
                val responseId: String? = (Json.parseToJsonElement(text) as JsonObject)["id"]
                    ?.jsonPrimitive?.content
                val eventName: String? = Json.parseToJsonElement(text)
                    .jsonObject["fields"]
                    ?.jsonObject["eventName"]?.jsonPrimitive?.content

                when {
                    responseId != null && responseId != "id" -> callsProcessing(
                        text = text,
                        responseId = responseId
                    )

                    eventName != null -> subscriptionsProcessing(text = text, eventName = eventName)
                }
            } catch (e: Exception) {
                Log.e("socketCheck", e.toString())
            }
        }

        //called when binary message received
        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.e("socketCheck", "onClosing()")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            //called when no more messages and the connection should be released
            Log.e("socketCheck", "onClosed()")
            if (shouldReconnect) reconnect()
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.e("socketCheck", "onFailure()")
            if (shouldReconnect) reconnect()
        }
    }

    private companion object {
        const val BASE_URL = "wss://eltex2025.rocket.chat/websocket"
        const val ROOMS_CALL_ID = "0be860f3-8cf9-4645-82c5-64aed6b6677b"
        const val ROOMS_SUB_ID = "333330f3-8cf9-4645-82c5-64aed6b6677b"
        const val SUBSCRIPTIONS_SUB_ID = "9agqd13-8cf9-46s5-8235-64aef6b6677b"
        const val ROOMS_CHANGED_EVENT_NAME = "rooms-changed"
        const val SUBSCRIPTIONS_CHANGED_EVENT_NAME = "subscriptions-changed"
        const val SUBSCRIPTIONS_CALL_ID = "71e86485-8a19-4645-82c5-64acd6b66777"
    }
}
