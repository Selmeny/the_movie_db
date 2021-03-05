package com.paulbrugman.app.themoviedb.utilities

import android.app.Application
import com.paulbrugman.app.themoviedb.BuildConfig.DEBUG
import timber.log.Timber

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        if (DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}