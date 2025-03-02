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
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive



@Serializable
data class UrlMetaData(
    val url: String,
    val meta: JsonObject = JsonObject(emptyMap())
)

@Serializable
data class MessageUrl(
    val urls: List<UrlMetaData>? = null
)


@Serializable
data class DateParamTest(
    @SerialName("\$date")
    val date: Long? = null
)

@Serializable
sealed class MessageResponse {
    @SerialName("_id")
    abstract val id: String?
    abstract val rid: String?
    abstract val ts: DateParamTest?
    abstract val u: UserTest?
    abstract val _updatedAt: DateParamTest?
    abstract val urls: List<UrlMetaData>?
    abstract val mentions: List<String>?
    abstract val channels: List<String>?
}

@Serializable
data class UserTest(
    @SerialName("_id")
    val id: String? = null,
    val username: String? = null,
    val name: String? = null
)

@Serializable
@SerialName("text")
data class TextMessage(
    @SerialName("_id")
    override val id: String? = null,
    override val rid: String? = null,
    override val ts: DateParamTest? = null,
    override val u: UserTest? = null,
    override val _updatedAt: DateParamTest? = null,
    override val urls: List<UrlMetaData>? = null,
    override val mentions: List<String>? = null,
    override val channels: List<String>? = null,
    val msg: String? = null,
    val md: List<MarkdownData>? = null
) : MessageResponse()

@Serializable
@SerialName("system")
data class SystemMessage(
    @SerialName("_id")
    override val id: String? = null,
    override val rid: String? = null,
    override val ts: DateParamTest? = null,
    override val u: UserTest? = null,
    override val _updatedAt: DateParamTest? = null,
    override val  urls: List<UrlMetaData>? = null,
    override val mentions: List<String>? = null,
    override val channels: List<String>? = null,
    val msg: String? = null,
    val t: String? = null,
    val groupable: Boolean? = null
) : MessageResponse()

@Serializable
data class MarkdownData(
    val type: String? = null,
    val value: List<MarkdownValue>? = null,
) {
    @Serializable
    sealed class MarkdownValue {
        @Serializable
        @SerialName("PLAIN_TEXT")
        data class PlainText(
            val type: String? = null,
            val value: String? = null,
        ) : MarkdownValue()

        @Serializable
        @SerialName("EMOJI")
        data class Emoji(
            val type: String? = null,
            val value: PlainText? = null,
            val shortCode: String? = null
        ) : MarkdownValue()


        @Serializable
        @SerialName("LINK")
        data class Link(
            val type: String? = null,
            val value: PlainText? = null,
            val shortCode: String? = null
        ) : MarkdownValue()

        @Serializable
        @SerialName("PARAGRAPH")
        data class Paragraph(
            val type: String? = null,
            val value: PlainText? = null,
            val shortCode: String? = null
        ) : MarkdownValue()
    }
}

@Serializable
data class FileData(
    @SerialName("_id")
    val id: String? = null,
    val name: String? = null,
    val type: String? = null,
    val size: Int? = null,
    val format: String? = null,
)

@Serializable
@SerialName("file")
sealed class FileMessage : MessageResponse() {
    abstract val msg: String?
    abstract val groupable: Boolean?
    abstract val file: FileData?
    abstract val files: List<FileData>?
    abstract val attachments: List<AttachmentData>?

    @Serializable
    @SerialName("image")
    data class ImageMessage(
        @SerialName("_id")
        override val id: String? = null,
        override val rid: String? = null,
        override val ts: DateParamTest? = null,
        override val u: UserTest? = null,
        override val _updatedAt: DateParamTest? = null,
        override val  urls: List<UrlMetaData>? = null,
        override val mentions: List<String>? = null,
        override val channels: List<String>? = null,
        override val msg: String? = null,
        override val groupable: Boolean? = null,
        override val file: FileData? = null,
        override val files: List<FileData>? = null,
        override val attachments: List<ImageAttachment>? = null,
    ) : FileMessage()

    @Serializable
    @SerialName("video")
    data class VideoMessage(
        @SerialName("_id")
        override val id: String? = null,
        override val rid: String? = null,
        override val ts: DateParamTest? = null,
        override val u: UserTest? = null,
        override val _updatedAt: DateParamTest? = null,
        override val urls: List<UrlMetaData>? = null,
        override val mentions: List<String>? = null,
        override val channels: List<String>? = null,
        override val msg: String? = null,
        override val groupable: Boolean? = null,
        override val file: FileData? = null,
        override val files: List<FileData>? = null,
        override val attachments: List<VideoAttachment>? = null,
    ) : FileMessage()

    @Serializable
    @SerialName("file")
    data class GenericFileMessage(
        @SerialName("_id")
        override val id: String? = null,
        override val rid: String? = null,
        override val ts: DateParamTest? = null,
        override val u: UserTest? = null,
        override val _updatedAt: DateParamTest? = null,
        override val urls: List<UrlMetaData>? = null,
        override val mentions: List<String>? = null,
        override val channels: List<String>? = null,
        override val msg: String? = null,
        override val groupable: Boolean? = null,
        override val file: FileData? = null,
        override val files: List<FileData>? = null,
        override val attachments: List<FileAttachment>? = null,
    ) : FileMessage()
}

// Обновляем MessageSerializer для поддержки системных сообщений
object MessageSerializer :
    JsonContentPolymorphicSerializer<MessageResponse>(MessageResponse::class) {
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
    abstract val ts: String?
    abstract val title: String?
    abstract val title_link: String?
    abstract val title_link_download: Boolean?
    abstract val type: String?
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
        override val ts: String? = null,
        override val title: String? = null,
        override val title_link: String? = null,
        override val title_link_download: Boolean? = null,
        override val type: String? = null,
        override val description: String? = null,
        override val descriptionMd: List<MarkdownData>? = null,
        val image_dimensions: ImageDimensions? = null,
        val image_preview: String? = null,
        val image_url: String? = null,
        val image_type: String? = null,
        val image_size: Int? = null,
    ) : AttachmentData() {

        @Serializable
        data class ImageDimensions(
            val width: Int? = null,
            val height: Int? = null,
        )
    }

    @Serializable
    data class VideoAttachment(
        override val ts: String? = null,
        override val title: String? = null,
        override val title_link: String? = null,
        override val title_link_download: Boolean? = null,
        override val type: String? = null,
        override val description: String? = null,
        override val descriptionMd: List<MarkdownData>? = null,
        val video_url: String? = null,
        val video_type: String? = null,
        val video_size: Int? = null,
    ) : AttachmentData()

    @Serializable
    data class FileAttachment(
        override val ts: String? = null,
        override val title: String? = null,
        override val title_link: String? = null,
        override val title_link_download: Boolean? = null,
        override val type: String? = null,
        override val description: String? = null,
        override val descriptionMd: List<MarkdownData>? = null,
        val format: String? = null,
        val size: Int? = null
    ) : AttachmentData()
}