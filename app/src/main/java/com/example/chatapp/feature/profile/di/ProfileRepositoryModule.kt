package com.example.chatapp.feature.profile.di

import com.example.chatapp.feature.profile.data.ProfileRepository
import com.example.chatapp.feature.profile.data.ProfileRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface ProfileRepositoryModule {

    @Binds
    fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository
}
