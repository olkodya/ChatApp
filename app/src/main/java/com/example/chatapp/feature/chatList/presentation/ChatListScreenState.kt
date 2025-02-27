package com.example.chatapp.feature.chatList.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Immutable
import com.example.chatapp.components.ErrorState
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Immutable
sealed class ChatListScreenState {

    @Immutable
    data class Content(
        val rooms: List<RoomState>
    ) : ChatListScreenState()

    @Immutable
    data object Loading : ChatListScreenState()

    @Immutable
    data class Error(val state: ErrorState) : ChatListScreenState()
}

@Immutable
data class RoomState(
    val id: String,
    val imageUrl: String?,
    private val type: String,
    val name: String?,
    val lastMassage: String?,
    val lastMessageDate: Long?,
    private val lastMessageAuthor: String?,
    private val isMeMessageAuthor: Boolean, // Don't work, not truth value
    val unreadMessagesNumber: Int,
    val userName: String?,
) {

    val showedName: String?
        get() = if (isMeMessageAuthor.not()) {
            lastMessageAuthor
        } else {
            null
        }


    val showedImageUrl: String
        get() {
            //name is null
            val i = if (type == "c") {
                "https://eltex2025.rocket.chat/avatar/room/$id"
            } else {
                "https://eltex2025.rocket.chat/avatar/$userName"
            }
            return i
        }

    val showedUnreadMessages: Int?
        get() = when {
            isMeMessageAuthor.not() && unreadMessagesNumber != 0 -> unreadMessagesNumber
            else -> null
        }

    val showedLastMessageAuthor: String?
        get() = when {
            type == "c" -> {
                if (isMeMessageAuthor) "Вы: "
                else if (lastMessageAuthor == null) ""
                else "${lastMessageAuthor}: "
            }

            else -> null
        }

    val numberOfCheckMark: Int?
        get() = when {
            (isMeMessageAuthor && unreadMessagesNumber > 0) -> 1
            (isMeMessageAuthor && unreadMessagesNumber == 0) -> 2
            else -> null
        }

    val showedLastMessageDate: String?
        @RequiresApi(Build.VERSION_CODES.O)
        get() = lastMessageDate?.let { timestamp ->
            val messageDateTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(timestamp),
                ZoneId.systemDefault()
            )
            val now = LocalDateTime.now()

            when {
                messageDateTime.toLocalDate() == now.toLocalDate() -> {
                    messageDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                }

                ChronoUnit.DAYS.between(
                    messageDateTime.toLocalDate(),
                    now.toLocalDate()
                ) < 7 -> {
                    when (messageDateTime.format(DateTimeFormatter.ofPattern("EE"))
                        .lowercase()) {
                        "mon" -> "Пн"
                        "tue" -> "Вт"
                        "wed" -> "Ср"
                        "thu" -> "Чт"
                        "fri" -> "Пт"
                        "sat" -> "Сб"
                        "sun" -> "Вс"
                        else -> messageDateTime.format(DateTimeFormatter.ofPattern("EE"))
                    }
                        .lowercase()
                        .replaceFirstChar { it.uppercase() }
                }

                else -> {
                    messageDateTime.format(DateTimeFormatter.ofPattern("dd.MM"))
                }
            }
        }
}
