package com.github.mazar1ni.tasktracker.core

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        // TODO: Log
        super.onCreate()

        Thread.setDefaultUncaughtExceptionHandler { _, _ ->
            // TODO: Log
        }
    }

    override fun onTerminate() {
        // TODO: Log
        super.onTerminate()
    }

}