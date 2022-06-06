package com.github.mazar1ni.tasktracker.core.database.interfaces

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

abstract class BaseEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID_FIELD_NAME)
    var id: Int? = null

    companion object {
        const val ID_FIELD_NAME = "id"
    }
}