package com.example.chatapp.feature.splashscreen.di

import com.example.chatapp.feature.splashscreen.domain.IsAuthorizedUseCase
import com.example.chatapp.feature.splashscreen.domain.IsAuthorizedUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface IsAuthorizedUseCaseModule {

    @Binds
    fun bindCheckAuthUseCase(impl: IsAuthorizedUseCaseImpl): IsAuthorizedUseCase
}