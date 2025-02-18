package com.example.chatapp.feature.authorization.di

import com.example.chatapp.feature.authorization.domain.CheckAuthUseCase
import com.example.chatapp.feature.authorization.domain.CheckAuthUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface CheckAuthUseCaseModule {

    @Binds
    fun bindCheckAuthUseCase(impl: CheckAuthUseCaseImpl): CheckAuthUseCase
}