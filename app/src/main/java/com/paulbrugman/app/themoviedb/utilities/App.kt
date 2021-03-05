package com.paulbrugman.app.themoviedb.utilities

import android.app.Application
import de.hdodenhof.circleimageview.BuildConfig
import timber.log.Timber

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}