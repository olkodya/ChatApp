package com.example.chatapp.feature.authorization.di

import com.example.chatapp.feature.authorization.domain.LoginUseCase
import com.example.chatapp.feature.authorization.domain.LoginUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface LoginUseCaseModule {

    @Binds
    fun bindLoginUseCase(impl: LoginUseCaseImpl): LoginUseCase
}
