package com.example.chatapp.feature.chat.di

import com.example.chatapp.feature.chat.data.ChatRepository
import com.example.chatapp.feature.chatList.data.ChatListRepository
import com.example.chatapp.feature.chatList.data.ChatListRepositoryImpl
import com.example.chatapp.feature.chatList.data.ChatRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface GetMessagesRepositoryModule {

    @Binds
    fun bindChatRepository(impl: ChatRepositoryImpl): ChatRepository
}
