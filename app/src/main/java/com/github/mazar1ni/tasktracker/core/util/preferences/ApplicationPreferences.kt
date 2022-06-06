package com.github.mazar1ni.tasktracker.core.util.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ApplicationPreferences @Inject constructor(private val sharedPreferences: SharedPreferences) {

    companion object {

        private const val preferencesFileName = "com.github.Mazar1ni.tasktracker.preferences"

        fun createEncryptedSharedPreferences(context: Context) =
            EncryptedSharedPreferences.create(
                context,
                preferencesFileName,
                MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

    }

    fun setString(type: PreferencesType, value: String) =
        runBlocking(Dispatchers.IO) {
            with(sharedPreferences.edit()) {
                putString(type.toString(), value)
                apply()
            }
        }

    fun setLong(type: PreferencesType, value: Long) =
        runBlocking(Dispatchers.IO) {
            with(sharedPreferences.edit()) {
                putLong(type.toString(), value)
                apply()
            }
        }

    fun getString(type: PreferencesType) =
        runBlocking(Dispatchers.IO) {
            sharedPreferences.getString(type.toString(), null)
        }

    fun getLong(type: PreferencesType) =
        runBlocking(Dispatchers.IO) {
            val value = sharedPreferences.getLong(type.toString(), -1L)
            if (value == -1L) null else value
        }

    fun contains(type: PreferencesType) =
        runBlocking(Dispatchers.IO) {
            sharedPreferences.contains(type.toString())
        }
}