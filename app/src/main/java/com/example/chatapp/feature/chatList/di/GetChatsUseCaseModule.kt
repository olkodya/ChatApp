package com.example.chatapp.feature.chatList.di

import com.example.chatapp.feature.chatList.domain.ObserveRoomsUseCase
import com.example.chatapp.feature.chatList.domain.ObserveRoomsUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface GetChatsUseCaseModule {

    @Binds
    fun bindGetChatsUseCase(impl: ObserveRoomsUseCaseImpl): ObserveRoomsUseCase
}
