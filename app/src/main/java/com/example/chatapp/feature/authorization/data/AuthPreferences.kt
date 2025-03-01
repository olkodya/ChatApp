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
import kotlinx.coroutines.runBlocking
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
            val password = preferences[KEY_PASSWORD_SHA256]
            val username = preferences[KEY_USERNAME]
            if (token != null && userId != null && password != null && username != null) {
                AuthData(token, userId, password, username)
            } else null
        }

    fun getAuthData(): AuthData? = runBlocking {
        val token = context.dataStore.updateData { it }[KEY_AUTH_TOKEN]
        val userId = context.dataStore.updateData { it }[KEY_USER_ID]
        val password = context.dataStore.updateData { it }[KEY_PASSWORD_SHA256]
        val username = context.dataStore.updateData { it }[KEY_USERNAME]
        return@runBlocking if (token != null && userId != null && password != null && username != null) {
            AuthData(token, userId, password, username)
        } else {
            null
        }
    }


    suspend fun saveAuthData(token: String, userId: String, password: String, username: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_AUTH_TOKEN] = token
            preferences[KEY_USER_ID] = userId
            preferences[KEY_PASSWORD_SHA256] = password
            preferences[KEY_USERNAME] = username
        }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }

    private companion object {
        private val KEY_AUTH_TOKEN = stringPreferencesKey("auth_token")
        private val KEY_USER_ID = stringPreferencesKey("user_id")
        private val KEY_PASSWORD_SHA256 = stringPreferencesKey("password_256")
        private val KEY_USERNAME = stringPreferencesKey("username")
    }
}

data class AuthData(
    val token: String,
    val userId: String,
    val password: String,
    val username: String,
)
