package com.github.mazar1ni.tasktracker.auth.di

import com.github.mazar1ni.tasktracker.auth.data.remote.AuthApi
import com.github.mazar1ni.tasktracker.auth.data.repository.AuthRepositoryImpl
import com.github.mazar1ni.tasktracker.auth.domain.repository.AuthRepository
import com.github.mazar1ni.tasktracker.core.util.Constants
import com.github.mazar1ni.tasktracker.core.util.NetworkManager
import com.github.mazar1ni.tasktracker.core.util.preferences.ApplicationPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        authApi: AuthApi,
        applicationPreferences: ApplicationPreferences
    ): AuthRepository = AuthRepositoryImpl(authApi, applicationPreferences)

    @Provides
    @Singleton
    fun provideAuthApi(): AuthApi = NetworkManager.create(Constants.SERVER_URL)

}