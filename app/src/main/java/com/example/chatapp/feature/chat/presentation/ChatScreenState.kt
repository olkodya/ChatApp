package com.example.chatapp.feature.chat.presentation

import androidx.compose.runtime.Immutable
import com.example.chatapp.components.ErrorState
import com.example.chatapp.feature.chat.di.MessageEntity
import com.example.chatapp.feature.chatList.presentation.RoomState.RoomTypeState

@Immutable
data class ChatScreenState(
    val textField: String = "",
    val messages: List<MessageState> = emptyList(),
    val isLoading: Boolean = false,
    val error: ErrorState? = null,
    val topBarState: TopBarState? = null,
) {
    data class TopBarState(
        val isLoading: Boolean = false,
        val chatType: RoomTypeState? = null,
        val chatName: String? = null,
        val chatAvatarUrl: String? = null,
        val numberOfMembers: String? = null,
    )
}

@Immutable
data class MessageState(
    val id: String,
    val isMeAuthor: Boolean,
    val messageAuthorName: String?,
    val messageTimestamp: Long,
    val messageType: MessageType,
) {

    @Immutable
    sealed class MessageType {
        abstract val text: String?

        @Immutable
        data class Text(
            override val text: String,
        ) : MessageType()

        @Immutable
        data class Image(
            override val text: String?,
            val ratio: Float,
            val imagePreviewUrl: String,
            val imageUrl: String,
        ) : MessageType()

        @Immutable
        data class Video(
            override val text: String?,
            val videoName: String,
            val videoType: String,
            val videoUrl: String,
        ) : MessageType()

        @Immutable
        data class File(
            override val text: String?,
            val fileName: String,
            val fileUrl: String,
        ) : MessageType()
    }
}

fun MessageEntity.toMessageState() = MessageState(
    id = id,
    isMeAuthor = isMeAuthor,
    messageAuthorName = messageAuthorName,
    messageTimestamp = messageTimestamp,
    messageType = messageType.toState()
)

fun MessageEntity.MessageType.toState() = when (this) {
    is MessageEntity.MessageType.File -> MessageState.MessageType.File(
        text = text,
        fileName = fileName,
        fileUrl = fileUrl
    )

    is MessageEntity.MessageType.Image -> MessageState.MessageType.Image(
        text = text,
        ratio = width.toFloat() / height.toFloat(),
        imagePreviewUrl = imagePreviewUrl,
        imageUrl = imageUrl
    )

    is MessageEntity.MessageType.Text -> MessageState.MessageType.Text(
        text = text
    )

    is MessageEntity.MessageType.Video -> MessageState.MessageType.Video(
        text = text,
        videoName = videoName,
        videoType = videoType,
        videoUrl = videoUrl
    )
}
