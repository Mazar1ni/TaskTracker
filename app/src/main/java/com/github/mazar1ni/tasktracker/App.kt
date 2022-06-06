package com.github.mazar1ni.tasktracker

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        // TODO: Log
        super.onCreate()

        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            Log.e("tasktracker", "${throwable.localizedMessage}\n${throwable.stackTraceToString()}")
        }
    }

    override fun onTerminate() {
        // TODO: Log
        super.onTerminate()
    }

}