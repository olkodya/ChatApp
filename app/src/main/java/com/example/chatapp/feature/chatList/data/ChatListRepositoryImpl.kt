package com.example.chatapp.feature.chatList.data

import android.util.Log
import com.example.chatapp.data.WebSocketDataStore
import com.example.chatapp.feature.authorization.data.AuthData
import com.example.chatapp.feature.authorization.data.AuthPreferences
import com.example.chatapp.feature.chatList.data.api.ChatListApi
import com.example.chatapp.feature.chatList.data.model.CreateChatRequest
import com.example.chatapp.feature.chatList.data.model.RoomResponse
import com.example.chatapp.feature.chatList.data.model.RoomsResponse
import com.example.chatapp.feature.chatList.data.model.RoomsResponseSubscription
import com.example.chatapp.feature.chatList.data.model.SubscriptionResponse
import com.example.chatapp.feature.chatList.data.model.SubscriptionsResponse
import com.example.chatapp.feature.chatList.data.model.SubscriptionsSubscriptionResponse
import com.example.chatapp.feature.chatList.data.model.UserListResponse
import com.example.chatapp.feature.chatList.data.model.WebSocketMessage
import com.example.chatapp.feature.chatList.domain.model.RoomEntity
import com.example.chatapp.feature.chatList.domain.model.toEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.OkHttpClient
import okio.IOException
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ChatListRepositoryImpl @Inject constructor(
    private val webSocketDataStore: WebSocketDataStore,
    private val api: ChatListApi,
    @Named("WebSocketOkHttpClient") private val okHttpClient: OkHttpClient,
    private val authPreferences: AuthPreferences,
    private val formattedJson: Json,
) : ChatListRepository {

    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var authData: AuthData

    private val roomsMutableStateFlow = MutableStateFlow<List<RoomEntity>?>(null)
    val roomsStateFlow: StateFlow<List<RoomEntity>?> = roomsMutableStateFlow.asStateFlow()


    private fun checkAndLoadConversationalistInfo() {
        roomsStateFlow.value?.forEach { room ->
            room.userId?.let { userId ->
                // TODO: Not use not viewModel's coroutines scopes
                repositoryScope.launch {
                    try {
                        val response = api.getUsersInfo(userId)
                        delay(1000)
                        roomsMutableStateFlow.value =
                            roomsMutableStateFlow.value?.map { currentRoom ->
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
                    }
                }
            }
        }
    }

    override suspend fun observeRooms(
        stateFlow: (StateFlow<List<RoomEntity>?>) -> Unit
    ) = withContext(Dispatchers.IO) {

        authData = authPreferences.getAuthData()
            ?: throw IOException("Auth data not set")
        webSocketDataStore.sendMessage(WebSocketMessage.RoomsGet(id = ROOMS_CALL_ID))
        webSocketDataStore.sendMessage(WebSocketMessage.SubscriptionsGet(id = SUBSCRIPTIONS_CALL_ID))
        webSocketDataStore.sendMessage(
            WebSocketMessage.RoomsSubscribe.roomsFactory(
                id = ROOMS_SUB_ID,
                userId = authData.userId,
            )
        )
        webSocketDataStore.sendMessage(
            WebSocketMessage.SubscriptionsSubscribe.subscriptionsFactory(
                id = SUBSCRIPTIONS_SUB_ID,
                userId = authData.userId,
            )
        )

        stateFlow(roomsStateFlow)

        webSocketDataStore.responseFlow.drop(1).collect { text ->
            if (text == null) return@collect
            onTextSocketProcessing(text)
        }
    }

    override suspend fun getUsers(): UserListResponse = api.getUsersList(count = USER_LIST_COUNT)
    override suspend fun createChat(username: String) =
        api.createDM(CreateChatRequest(username = username))

    private fun callsProcessing(text: String, responseId: String) = when (responseId) {
        ROOMS_CALL_ID -> {
            try {
                val entities: List<RoomEntity> =
                    formattedJson.decodeFromString(
                        RoomsResponse.serializer(),
                        text
                    ).result.update.map { it.toEntity(0, authData.userId, authData.username) }
                roomsMutableStateFlow.value = entities
                Log.d("roomsMutableStateFlow", "roomsMutableStateFlow?.value?.size")
                checkAndLoadConversationalistInfo()
            } catch (e: Exception) {
                print(e.toString())
                Log.d("roomsMutableStateFlow", e.toString())

            }
        }

        SUBSCRIPTIONS_CALL_ID -> {
            try {
                val subscriptions: SubscriptionsResponse =
                    formattedJson.decodeFromString(SubscriptionsResponse.serializer(), text)
                Log.d("d", subscriptions.toString())
                subscriptions.result.update.meagreSubscriptionsToRoomsStateFlow()
            } catch (e: Exception) {
                Log.d("s", e.toString())
            }
        }

        else -> {}
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
                    ).toEntity(0, authData.userId, authData.username)
                } catch (_: Exception) {
                    null
                }
            }

        roomsMutableStateFlow.update { currentRooms ->
            val roomsMap = currentRooms?.associateBy { it.id }?.toMutableMap()
            updatedRooms.forEach { room ->
                roomsMap?.get(room.id)?.let { element ->
                    roomsMap[room.id] = element.copy(
                        id = room.id,
                        type = room.type,
                        lastMessageContent = room.lastMessageContent,
                        lastUpdateTimestamp = room.lastUpdateTimestamp,
                        lastMessageAuthor = room.lastMessageAuthor,
                        lastMessageAuthorId = room.lastMessageAuthorId,
                        isMeMessageAuthor = room.isMeMessageAuthor,
                        lastMessageType = room.lastMessageType
                    )
                }
                if (roomsMap?.get(room.id) == null) {
                    roomsMap?.set(room.id, room)
                }
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

    fun onTextSocketProcessing(text: String) {
        try {
            val responseId: String? = (Json.parseToJsonElement(text) as JsonObject)["id"]
                ?.jsonPrimitive?.content

            val eventName: String? = Json.parseToJsonElement(text)
                    .jsonObject["fields"]
                    ?.jsonObject["eventName"]?.jsonPrimitive?.content

            Log.d("roomsMutableStateFlow", responseId.toString())
//            Log.e("12351235421", "$responseId++++$eventName")
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

    private companion object {
        const val ROOMS_CALL_ID = "0be860f3-8cf9-4645-82c5-64aed6b6677b"
        const val ROOMS_SUB_ID = "333330f3-8cf9-4645-82c5-64aed6b6677b"
        const val SUBSCRIPTIONS_SUB_ID = "9agqd13-8cf9-46s5-8235-64aef6b6677b"
        const val ROOMS_CHANGED_EVENT_NAME = "rooms-changed"
        const val SUBSCRIPTIONS_CHANGED_EVENT_NAME = "subscriptions-changed"
        const val SUBSCRIPTIONS_CALL_ID = "71e86485-8a19-4645-82c5-64acd6b66777"
        const val USER_LIST_COUNT = 500
    }
}
