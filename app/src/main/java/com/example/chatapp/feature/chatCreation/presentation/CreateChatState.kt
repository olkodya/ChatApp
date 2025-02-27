package com.example.chatapp.feature.chatCreation.presentation

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import com.example.chatapp.components.ErrorState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.immutableListOf
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Immutable
data class CreateChatState(
    private val users: ImmutableList<UserState> = persistentListOf(),
    val searchQuery: String = "",
    val errorState: ErrorState? = null,
    val isLoading: Boolean = true,
) {

    /**
     * Фильтрация
     * Запрос на поиск в списке пользователей со стороны сервера не работает,
     * данные приходят неотфильтрованные, поэтому поиск осушествляется локально.
     * Из-за локальной фильтрации выполнить пагинацию не получится, поэтому в запросе
     * к серверу передаем параметр count = 500, чтобы получить сразу всех пользователей.
     * Поскольку фильтрация осуществляется локально, смысла выполнять задержку в 200 мс и
     * задавать ограничение в 2 символа для поиска нет
     **/

    val filteredUsersByQuery: ImmutableList<UserState>
        get() = users.filter { user ->
            val nameLowerCase: String = user.name.toLowerCase(Locale.current)
            val searchQueryLowerCase: String = searchQuery.toLowerCase(Locale.current)
            nameLowerCase.contains(searchQueryLowerCase)
        }.toImmutableList()
}

@Immutable
data class UserState(
    val id: String,
    val avatarUrl: String,
    val name: String,
    val username: String,
)
