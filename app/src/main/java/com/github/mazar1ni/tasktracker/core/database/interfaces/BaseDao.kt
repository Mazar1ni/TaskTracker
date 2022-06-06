package com.github.mazar1ni.tasktracker.core.database.interfaces

import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
abstract class BaseDao<T>(protected val tableName: String) {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(entity: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertList(entities: List<T>)

    @Delete
    abstract fun delete(entity: T)

    @Delete
    abstract fun deleteList(entities: List<T>)

    @RawQuery
    fun deleteAll() = rawQuery(SimpleSQLiteQuery("DELETE FROM $tableName"))

    @RawQuery
    fun getAll(): List<T>? = rawQuery(SimpleSQLiteQuery("SELECT * FROM $tableName"))

    @RawQuery
    fun getById(id: Int): T? =
        rawQueryOneItem(SimpleSQLiteQuery("SELECT * FROM $tableName WHERE ${BaseEntity.ID_FIELD_NAME} = $id LIMIT 1"))

    @RawQuery
    protected abstract fun rawQuery(query: SupportSQLiteQuery): List<T>?

    @RawQuery
    protected abstract fun rawQueryOneItem(query: SupportSQLiteQuery): T?
}