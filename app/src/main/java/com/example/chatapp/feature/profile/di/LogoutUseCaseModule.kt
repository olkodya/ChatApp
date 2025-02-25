package com.example.chatapp.feature.profile.di

import com.example.chatapp.feature.profile.domain.LogoutUseCase
import com.example.chatapp.feature.profile.domain.LogoutUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface LogoutUseCaseModule {

    @Binds
    fun bindLogoutUseCase(impl: LogoutUseCaseImpl): LogoutUseCase
}
