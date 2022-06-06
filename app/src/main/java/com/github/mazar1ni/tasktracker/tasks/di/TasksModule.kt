package com.github.mazar1ni.tasktracker.tasks.di

import com.github.mazar1ni.tasktracker.core.database.AppDatabase
import com.github.mazar1ni.tasktracker.core.util.Constants
import com.github.mazar1ni.tasktracker.core.util.NetworkManager
import com.github.mazar1ni.tasktracker.core.util.preferences.ApplicationPreferences
import com.github.mazar1ni.tasktracker.tasks.data.local.delete_tasks.DeletedTasksLocalDataSource
import com.github.mazar1ni.tasktracker.tasks.data.local.tasks.TasksLocalDataSource
import com.github.mazar1ni.tasktracker.tasks.data.remote.TasksApi
import com.github.mazar1ni.tasktracker.tasks.data.repository.TasksRepositoryImpl
import com.github.mazar1ni.tasktracker.tasks.domain.repository.TasksRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TasksModule {

    @Provides
    @Singleton
    fun provideTasksRepository(
        tasksApi: TasksApi,
        applicationPreferences: ApplicationPreferences,
        tasksLocalDataSource: TasksLocalDataSource,
        deletedTasksLocalDataSource: DeletedTasksLocalDataSource
    ): TasksRepository = TasksRepositoryImpl(
        tasksApi,
        applicationPreferences,
        tasksLocalDataSource,
        deletedTasksLocalDataSource
    )

    @Provides
    @Singleton
    fun provideTasksApi(): TasksApi = NetworkManager.create(Constants.SERVER_URL)

    @Provides
    @Singleton
    fun provideTasksLocalDataSource(appDatabase: AppDatabase) =
        TasksLocalDataSource(appDatabase.tasksDao())

    @Provides
    @Singleton
    fun provideDeleteTasksLocalDataSource(appDatabase: AppDatabase) =
        DeletedTasksLocalDataSource(appDatabase.deleteTasksDao())

}