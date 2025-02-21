package com.example.chatapp.feature.profile.data

import com.example.chatapp.feature.profile.data.model.ProfileInfo

interface ProfileRepository {
    suspend fun getProfileInfo(): ProfileInfo
    suspend fun logout()
}