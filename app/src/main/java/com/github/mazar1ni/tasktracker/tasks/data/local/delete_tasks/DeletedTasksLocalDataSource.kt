package com.github.mazar1ni.tasktracker.tasks.data.local.delete_tasks

import com.github.mazar1ni.tasktracker.core.database.interfaces.BaseLocalDataSource

class DeletedTasksLocalDataSource(private val deletedTasksDao: DeletedTasksDao) :
    BaseLocalDataSource<DeletedTasksEntity>(deletedTasksDao)