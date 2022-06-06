package com.github.mazar1ni.tasktracker.core.util

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

object DatabaseManager {
    inline fun <reified T : RoomDatabase> create(
        context: Context,
        dbName: String,
        password: String? = null
    ) = Room.databaseBuilder(
        context.applicationContext,
        T::class.java,
        dbName
    ).addCallback(object : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            // TODO: add log version
            super.onOpen(db)
        }
    }).run {

        if (!password.isNullOrEmpty())
            openHelperFactory(SupportFactory(SQLiteDatabase.getBytes(password.toCharArray())))

        build()
    }
}