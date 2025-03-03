package com.example.chatapp.feature.chat.presentation

import android.net.Uri
import androidx.compose.runtime.Immutable
import com.example.chatapp.components.ErrorState
import com.example.chatapp.feature.chat.di.MessageEntity
import com.example.chatapp.feature.chat.domain.model.ChatInfoEntity

@Immutable
data class ChatScreenState(
    val textField: String = "",
    val messages: List<MessageState> = emptyList(),
    val isLoading: Boolean = false,
    val error: ErrorState? = null,
    val topBarState: TopBarState? = null,
    val selectedImages: List<Uri?>? = null,
) {
    data class TopBarState(
        val isLoading: Boolean = true,
        val chatType: String? = null,
        val chatName: String? = null,
        val chatAvatarUrl: String? = null,
        val numberOfMembers: Int? = null,
    )
}

fun ChatInfoEntity.topTopBarState(roomId: String): ChatScreenState.TopBarState {


    return ChatScreenState.TopBarState(
        isLoading = false,
        chatType = chatType,
        chatName = chatName,
        chatAvatarUrl = if (chatType == "c") {
            "https://eltex2025.rocket.chat/avatar/room/$roomId"
        } else {
            "https://eltex2025.rocket.chat/avatar/$username"
        },
        numberOfMembers = numberOfMembers,
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
            val fileName: String?,
            val fileUrl: String,
        ) : MessageType()

        @Immutable
        data class System(
            override val text: String,
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

    is MessageEntity.MessageType.SystemMessage -> MessageState.MessageType.System(
        text = text
    )
}
