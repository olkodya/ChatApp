package com.example.chatapp.feature.chat.di

import com.example.chatapp.feature.chat.data.model.AttachmentData
import com.example.chatapp.feature.chat.data.model.FileMessage
import com.example.chatapp.feature.chat.data.model.MessageResponse
import com.example.chatapp.feature.chat.data.model.TextMessage

data class MessageEntity(
    val id: String,
    val messageAuthorId: String,
    val messageAuthorName: String,
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
            val fileName: String,
            val fileUrl: String,
        ) : MessageType()
    }
}

fun MessageResponse.toEntity(loggedUserId: String) = MessageEntity(
    id = id,
    messageAuthorName = u.name,
    isMeAuthor = u.id == loggedUserId,
    messageAuthorId = u.id,
    messageTimestamp = ts.date,
    messageType = when (this) {
        is TextMessage -> {
            MessageEntity.MessageType.Text(text = msg)
        }

        is FileMessage -> {
            when (this) {
                is FileMessage.ImageMessage -> {
                    val attachment: AttachmentData.ImageAttachment =
                        attachments.firstOrNull() ?: error("FileMessage without ImageAttachment")
                    MessageEntity.MessageType.Image(
                        text = attachment.desc,
                        width = attachment.image_dimensions.width,
                        height = attachment.image_dimensions.height,
                        imagePreviewUrl = "https://eltex2025.rocket.chat" + attachment.image_preview,
                        imageUrl = "https://eltex2025.rocket.chat" + attachment.image_url,
                    )
                }

                is FileMessage.VideoMessage -> {
                    val attachment: AttachmentData.VideoAttachment =
                        attachments.firstOrNull() ?: error("FileMessage without VideoAttachment")
                    MessageEntity.MessageType.Video(
                        text = attachment.desc,
                        videoName = attachment.title,
                        videoType = attachment.video_type,
                        videoUrl = "https://eltex2025.rocket.chat" + attachment.title_link
                    )
                }

                is FileMessage.GenericFileMessage -> {
                    val attachment: AttachmentData.FileAttachment =
                        attachments.firstOrNull() ?: error("FileMessage without FileAttachment")
                    MessageEntity.MessageType.File(
                        text = attachment.desc,
                        fileName = attachment.title,
                        fileUrl = "https://eltex2025.rocket.chat" + attachment.title_link
                    )
                }
            }
        }
    },
)
