package com.example.chatapp.feature.chatList.di

import com.example.chatapp.feature.chatList.data.ChatListRepository
import com.example.chatapp.feature.chatList.data.ChatListRepositoryImpl
import com.example.chatapp.feature.profile.data.ProfileRepository
import com.example.chatapp.feature.profile.data.ProfileRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface ChatListRepositoryModule {

    @Binds
    fun bindChatListRepository(impl: ChatListRepositoryImpl): ChatListRepository
}
