package com.example.chatapp.feature.chatList.di

import com.example.chatapp.feature.chatList.domain.GetUsersUseCase
import com.example.chatapp.feature.chatList.domain.GetUsersUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface GetUsersUseCaseModule {

    @Binds
    fun bindGetChatsUseCase(impl: GetUsersUseCaseImpl): GetUsersUseCase
}
