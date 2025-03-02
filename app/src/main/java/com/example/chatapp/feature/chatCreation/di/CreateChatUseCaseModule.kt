package com.example.chatapp.feature.chatCreation.di

import com.example.chatapp.feature.chatCreation.domain.CreateChatUseCase
import com.example.chatapp.feature.chatCreation.domain.CreateChatUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface CreateChatUseCaseModule {

    @Binds
    fun bindCreateChatUseCase(impl: CreateChatUseCaseImpl): CreateChatUseCase
}
