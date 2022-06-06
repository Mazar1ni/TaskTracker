package com.github.mazar1ni.tasktracker.core.di

import android.content.Context
import android.content.SharedPreferences
import com.github.mazar1ni.tasktracker.core.database.AppDatabase
import com.github.mazar1ni.tasktracker.core.util.Constants
import com.github.mazar1ni.tasktracker.core.util.DatabaseManager
import com.github.mazar1ni.tasktracker.core.util.NavigationUtil
import com.github.mazar1ni.tasktracker.core.util.preferences.ApplicationPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNavigationUtil() = NavigationUtil()

    @Provides
    @Singleton
    fun provideEncryptedSharedPreferences(@ApplicationContext context: Context) =
        ApplicationPreferences.createEncryptedSharedPreferences(context)

    @Provides
    @Singleton
    fun provideApplicationPreferences(sharedPreferences: SharedPreferences) =
        ApplicationPreferences(sharedPreferences)

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        DatabaseManager.create(context, Constants.DB_NAME)
}