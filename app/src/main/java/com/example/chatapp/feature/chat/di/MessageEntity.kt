package com.example.chatapp.feature.chat.di

import com.example.chatapp.feature.chat.data.model.AttachmentData
import com.example.chatapp.feature.chat.data.model.FileMessage
import com.example.chatapp.feature.chat.data.model.MessageResponse
import com.example.chatapp.feature.chat.data.model.SystemMessage
import com.example.chatapp.feature.chat.data.model.TextMessage
import com.example.chatapp.feature.chat.di.MessageEntity.MessageType.File
import com.example.chatapp.feature.chat.di.MessageEntity.MessageType.Image
import com.example.chatapp.feature.chat.di.MessageEntity.MessageType.Text
import com.example.chatapp.feature.chat.di.MessageEntity.MessageType.Video

data class MessageEntity(
    val id: String,
    val messageAuthorId: String,
    val messageAuthorName: String?,
    val isMeAuthor: Boolean,
    val messageTimestamp: Long,
    val messageType: MessageType,
) {

    sealed class MessageType {

        data class Text(
            val text: String,
        ) : MessageType()

        data class Image(
            val text: String?,
            val width: Int,
            val height: Int,
            val imagePreviewUrl: String,
            val imageUrl: String,
        ) : MessageType()

        data class Video(
            val text: String?,
            val videoName: String,
            val videoType: String,
            val videoUrl: String,
        ) : MessageType()

        data class File(
            val text: String?,
            val fileName: String?,
            val fileUrl: String,
        ) : MessageType()

        data class SystemMessage(
            val text: String,
        ) : MessageType()

    }
}

fun MessageResponse.toEntity(loggedUserId: String) = MessageEntity(
    id = requireNotNull(id),
    messageAuthorName = requireNotNull(u?.name ?: ""),
    isMeAuthor = u?.id == loggedUserId,
    messageAuthorId = requireNotNull(u?.id ?: ""),
    messageTimestamp = requireNotNull(ts?.date ?: 0),
    messageType = when (this) {
        is TextMessage -> {
            Text(text = requireNotNull(msg))
        }

        is FileMessage -> {
            when (this) {
                is FileMessage.ImageMessage -> {
                    val attachment: AttachmentData.ImageAttachment =
                        attachments?.firstOrNull() ?: error("FileMessage without ImageAttachment")
                    Image(
                        text = attachment.desc,
                        width = requireNotNull(attachment.image_dimensions?.width),
                        height = requireNotNull(attachment.image_dimensions.height),
                        imagePreviewUrl = "https://eltex2025.rocket.chat" + attachment.image_preview,
                        imageUrl = "https://eltex2025.rocket.chat" + attachment.image_url,
                    )
                }

                is FileMessage.VideoMessage -> {
                    val attachment: AttachmentData.VideoAttachment =
                        attachments?.firstOrNull() ?: error("FileMessage without VideoAttachment")
                    Video(
                        text = attachment.desc,
                        videoName = requireNotNull(attachment.title ?: ""),
                        videoType = requireNotNull(attachment.video_type ?: ""),
                        videoUrl = "https://eltex2025.rocket.chat" + attachment.title_link
                    )
                }

                is FileMessage.GenericFileMessage -> {
                    val attachment: AttachmentData.FileAttachment =
                        attachments?.firstOrNull() ?: error("FileMessage without FileAttachment")
                    File(
                        text = attachment.desc,
                        fileName = attachment.title,
                        fileUrl = "https://eltex2025.rocket.chat" + attachment.title_link
                    )
                }
            }
        }

        is SystemMessage -> MessageEntity.MessageType.SystemMessage(text = t + msg)
    },
)
