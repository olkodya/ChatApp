package com.example.chatapp.feature.chat.di

import com.example.chatapp.feature.chat.data.ChatRepository
import com.example.chatapp.feature.chat.domain.GetRoomInfoUseCase
import com.example.chatapp.feature.chat.domain.GetRoomInfoUseCaseImpl
import com.example.chatapp.feature.chatList.data.ChatRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface GetRoomInfoUseCaseModule {

    @Binds
    fun bindRoomInfoUseCase(impl: GetRoomInfoUseCaseImpl): GetRoomInfoUseCase
}
