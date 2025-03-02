package com.example.chatapp.feature.chat.di

import com.example.chatapp.feature.chat.domain.CreateMessageUseCase
import com.example.chatapp.feature.chat.domain.CreateMessageUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface CreateMessageUseCaseModule {

    @Binds
    fun bindCreateMessageModule(impl: CreateMessageUseCaseImpl): CreateMessageUseCase
}
