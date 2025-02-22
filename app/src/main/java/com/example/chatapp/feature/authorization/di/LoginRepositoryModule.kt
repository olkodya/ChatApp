package com.example.chatapp.feature.authorization.di

import com.example.chatapp.feature.authorization.data.LoginRepository
import com.example.chatapp.feature.authorization.data.LoginRepositoryImpl

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface LoginRepositoryModule {

    @Binds
    fun bindLoginRepository(impl: LoginRepositoryImpl): LoginRepository
}
