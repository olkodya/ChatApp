package com.example.chatapp.feature.profile.di

import com.example.chatapp.feature.profile.domain.GetProfileInfoUseCase
import com.example.chatapp.feature.profile.domain.GetProfileInfoUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface GetProfileInfoUseCaseModule {

    @Binds
    fun bindGetProfileInfoUseCase(impl: GetProfileInfoUseCaseImpl): GetProfileInfoUseCase
}
