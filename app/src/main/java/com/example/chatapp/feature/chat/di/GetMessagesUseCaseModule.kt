package com.example.chatapp.feature.chat.di

import com.example.chatapp.feature.chat.domain.ObserveMessagesUse
import com.example.chatapp.feature.chat.domain.ObserveMessagesUseImpl
import com.example.chatapp.feature.chatList.data.ChatListRepository
import com.example.chatapp.feature.chatList.data.ChatListRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface GetMessagesUseCaseModule {

    @Binds
    fun bindGetChatsUseCase(impl: ObserveMessagesUseImpl): ObserveMessagesUse
}
