package com.example.chatapp.feature.authorization.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

@Singleton
class AuthPreferences @Inject constructor(
    private val context: Context
) {
    val authData: Flow<AuthData?> = context.dataStore.data
        .catch { exception ->
            emit(emptyPreferences())
        }.map { preferences ->
            val token = preferences[KEY_AUTH_TOKEN]
            val userId = preferences[KEY_USER_ID]
            if (token != null && userId != null) {
                AuthData(token, userId)
            } else null
        }

    suspend fun getAuthData(): AuthData? {
        val token = context.dataStore.updateData { it }[KEY_AUTH_TOKEN]
        val userId = context.dataStore.updateData { it }[KEY_USER_ID]
        return if (token != null && userId != null) {
            AuthData(token, userId)
        } else {
            null
        }
    }

    suspend fun saveAuthData(token: String, userId: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_AUTH_TOKEN] = token
            preferences[KEY_USER_ID] = userId
        }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }

    companion object {
        private val KEY_AUTH_TOKEN = stringPreferencesKey("auth_token")
        private val KEY_USER_ID = stringPreferencesKey("user_id")
    }
}

data class AuthData(
    val token: String,
    val userId: String,
)