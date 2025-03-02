package com.example.chatapp.feature.chat.di

import com.example.chatapp.feature.chat.domain.GetRoomInfoUseCase
import com.example.chatapp.feature.chat.domain.GetRoomInfoUseCaseImpl
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
