package com.github.mazar1ni.tasktracker.core.database.interfaces

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseLocalDataSource<T : BaseEntity>(private val dao: BaseDao<T>) {

    suspend fun insert(entity: T) =
        withContext(Dispatchers.IO) {
            dao.insert(entity)
        }

    suspend fun insertList(entities: List<T>) =
        withContext(Dispatchers.IO) {
            dao.insertList(entities)
        }

    suspend fun delete(entity: T) =
        withContext(Dispatchers.IO) {
            dao.delete(entity)
        }

    suspend fun deleteList(entities: List<T>) =
        withContext(Dispatchers.IO) {
            dao.deleteList(entities)
        }

    suspend fun deleteAll() =
        withContext(Dispatchers.IO) {
            dao.deleteAll()
        }

    suspend fun getAll(): List<T>? =
        withContext(Dispatchers.IO) {
            dao.getAll()
        }

    suspend fun getById(id: Int): T? =
        withContext(Dispatchers.IO) {
            dao.getById(id)
        }

}