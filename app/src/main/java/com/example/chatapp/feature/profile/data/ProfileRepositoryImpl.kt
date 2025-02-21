package com.example.chatapp.feature.profile.data

import com.example.chatapp.feature.profile.data.api.ProfileApi
import com.example.chatapp.feature.profile.data.model.ProfileInfo
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: ProfileApi
) : ProfileRepository {
    override suspend fun getProfileInfo(): ProfileInfo = api.getUserInfo()
    override suspend fun logout() = api.logout()
}