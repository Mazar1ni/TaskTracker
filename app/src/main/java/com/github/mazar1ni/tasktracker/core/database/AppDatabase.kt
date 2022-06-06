package com.github.mazar1ni.tasktracker.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.mazar1ni.tasktracker.core.database.AppDatabase.Companion.DB_VERSION
import com.github.mazar1ni.tasktracker.tasks.data.local.delete_tasks.DeletedTasksDao
import com.github.mazar1ni.tasktracker.tasks.data.local.delete_tasks.DeletedTasksEntity
import com.github.mazar1ni.tasktracker.tasks.data.local.tasks.TasksDao
import com.github.mazar1ni.tasktracker.tasks.data.local.tasks.TasksEntity

@Database(entities = [TasksEntity::class, DeletedTasksEntity::class], version = DB_VERSION)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tasksDao(): TasksDao
    abstract fun deleteTasksDao(): DeletedTasksDao

    companion object {
        const val DB_VERSION = 1
    }

}