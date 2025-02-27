package com.example.chatapp.feature.profile.domain

import com.example.chatapp.feature.profile.data.ProfileRepository
import com.example.chatapp.feature.profile.data.model.toEntity
import javax.inject.Inject

class GetProfileInfoUseCaseImpl @Inject constructor(
    private val repository: ProfileRepository
) : GetProfileInfoUseCase {

    override suspend fun invoke(userId: String, token: String): ProfileEntity {
        val res = repository.getProfileInfo()
            .toEntity()
        return res
    }
}
