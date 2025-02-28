package com.example.chatapp.feature.chat.data.model

import com.example.chatapp.feature.chat.data.model.AttachmentData.FileAttachment
import com.example.chatapp.feature.chat.data.model.AttachmentData.ImageAttachment
import com.example.chatapp.feature.chat.data.model.AttachmentData.VideoAttachment
import com.example.chatapp.feature.chat.data.model.FileMessage.GenericFileMessage
import com.example.chatapp.feature.chat.data.model.FileMessage.ImageMessage
import com.example.chatapp.feature.chat.data.model.FileMessage.VideoMessage
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class MessagesResponse(
    val msg: String,
    val id: String?,
    val result: MessagesResult?
)

@Serializable
data class MessagesResult(
    val messages: List<@Serializable(with = MessageSerializer::class) MessageResponse>?,
    val unreadNotLoaded: Int?
)

@Serializable
data class DateParamTest(
    @SerialName("\$date")
    val date: Long
)

@Serializable
sealed class MessageResponse {
    @SerialName("_id")
    abstract val id: String
    abstract val rid: String
    abstract val ts: DateParamTest
    abstract val u: UserTest
    abstract val _updatedAt: DateParamTest
    abstract val urls: List<String>
    abstract val mentions: List<String>
    abstract val channels: List<String>
}

@Serializable
data class UserTest(
    @SerialName("_id")
    val id: String,
    val username: String,
    val name: String? = null
)

@Serializable
@SerialName("text")
data class TextMessage(
    @SerialName("_id")
    override val id: String,
    override val rid: String,
    override val ts: DateParamTest,
    override val u: UserTest,
    override val _updatedAt: DateParamTest,
    override val urls: List<String> = emptyList(),
    override val mentions: List<String> = emptyList(),
    override val channels: List<String> = emptyList(),
    val msg: String,
    val md: List<MarkdownData>? = null
) : MessageResponse()

@Serializable
@SerialName("system")
data class SystemMessage(
    @SerialName("_id")
    override val id: String,
    override val rid: String,
    override val ts: DateParamTest,
    override val u: UserTest,
    override val _updatedAt: DateParamTest,
    override val urls: List<String> = emptyList(),
    override val mentions: List<String> = emptyList(),
    override val channels: List<String> = emptyList(),
    val msg: String,
    val t: String, // тип системного сообщения (например "uj" для user joined)
    val groupable: Boolean = false
) : MessageResponse()

@Serializable
data class MarkdownData(
    val type: String,
    val value: List<MarkdownValue>? = null,
) {
    @Serializable
    sealed class MarkdownValue {
        @Serializable
        @SerialName("PLAIN_TEXT")
        data class PlainText(
            val type: String,
            val value: String? = null
        ) : MarkdownValue()

        @Serializable
        @SerialName("EMOJI")
        data class Emoji(
            val type: String,
            val value: PlainText,
            val shortCode: String
        ) : MarkdownValue()
    }
}

@Serializable
data class FileData(
    @SerialName("_id")
    val id: String,
    val name: String,
    val type: String,
    val size: Int,
    val format: String
)

@Serializable
@SerialName("file")
sealed class FileMessage : MessageResponse() {
    abstract val msg: String
    abstract val groupable: Boolean
    abstract val file: FileData
    abstract val files: List<FileData>
    abstract val attachments: List<AttachmentData>

    @Serializable
    @SerialName("image")
    data class ImageMessage(
        @SerialName("_id")
        override val id: String,
        override val rid: String,
        override val ts: DateParamTest,
        override val u: UserTest,
        override val _updatedAt: DateParamTest,
        override val urls: List<String> = emptyList(),
        override val mentions: List<String> = emptyList(),
        override val channels: List<String> = emptyList(),
        override val msg: String = "",
        override val groupable: Boolean,
        override val file: FileData,
        override val files: List<FileData>,
        override val attachments: List<ImageAttachment>
    ) : FileMessage()

    @Serializable
    @SerialName("video")
    data class VideoMessage(
        @SerialName("_id")
        override val id: String,
        override val rid: String,
        override val ts: DateParamTest,
        override val u: UserTest,
        override val _updatedAt: DateParamTest,
        override val urls: List<String> = emptyList(),
        override val mentions: List<String> = emptyList(),
        override val channels: List<String> = emptyList(),
        override val msg: String = "",
        override val groupable: Boolean,
        override val file: FileData,
        override val files: List<FileData>,
        override val attachments: List<VideoAttachment>
    ) : FileMessage()

    @Serializable
    @SerialName("file")
    data class GenericFileMessage(
        @SerialName("_id")
        override val id: String,
        override val rid: String,
        override val ts: DateParamTest,
        override val u: UserTest,
        override val _updatedAt: DateParamTest,
        override val urls: List<String> = emptyList(),
        override val mentions: List<String> = emptyList(),
        override val channels: List<String> = emptyList(),
        override val msg: String = "",
        override val groupable: Boolean,
        override val file: FileData,
        override val files: List<FileData>,
        override val attachments: List<FileAttachment>
    ) : FileMessage()
}

// Обновляем MessageSerializer для поддержки системных сообщений
object MessageSerializer : JsonContentPolymorphicSerializer<MessageResponse>(MessageResponse::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out MessageResponse> {
        val json = element.jsonObject
        return when {
            json.containsKey("t") -> SystemMessage.serializer() // Если есть поле t - это системное сообщение
            !json.containsKey("file") -> TextMessage.serializer()
            json["file"]?.jsonObject?.get("type")?.jsonPrimitive?.content?.startsWith("image") == true ->
                ImageMessage.serializer()
            json["file"]?.jsonObject?.get("type")?.jsonPrimitive?.content?.startsWith("video") == true ->
                VideoMessage.serializer()
            else -> GenericFileMessage.serializer()
        }
    }
}

// Вспомогательные классы для вложений
@Serializable
sealed class AttachmentData {
    abstract val ts: String
    abstract val title: String
    abstract val title_link: String
    abstract val title_link_download: Boolean
    abstract val type: String
    abstract val description: String?
    abstract val descriptionMd: List<MarkdownData>?

    val desc: String?
        get() = if (description.isNullOrEmpty()) {
            null
        } else {
            description
        }


    @Serializable
    data class ImageAttachment(
        override val ts: String,
        override val title: String,
        override val title_link: String,
        override val title_link_download: Boolean,
        override val type: String,
        override val description: String? = null,
        override val descriptionMd: List<MarkdownData>? = null,
        val image_dimensions: ImageDimensions,
        val image_preview: String,
        val image_url: String,
        val image_type: String,
        val image_size: Int
    ) : AttachmentData() {

        @Serializable
        data class ImageDimensions(
            val width: Int,
            val height: Int,
        )
    }

    @Serializable
    data class VideoAttachment(
        override val ts: String,
        override val title: String,
        override val title_link: String,
        override val title_link_download: Boolean,
        override val type: String,
        override val description: String? = null,
        override val descriptionMd: List<MarkdownData>? = null,
        val video_url: String,
        val video_type: String,
        val video_size: Int
    ) : AttachmentData()

    @Serializable
    data class FileAttachment(
        override val ts: String,
        override val title: String,
        override val title_link: String,
        override val title_link_download: Boolean,
        override val type: String,
        override val description: String? = null,
        override val descriptionMd: List<MarkdownData>? = null,
        val format: String? = null,
        val size: Int? = null
    ) : AttachmentData()
}