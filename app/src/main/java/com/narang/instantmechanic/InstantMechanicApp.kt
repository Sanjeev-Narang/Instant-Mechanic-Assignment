package com.narang.instantmechanic

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class InstantMechanicApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.i("InstantMechanicApp created - Timber planted")

        // Global crash tracer - so even uncaught exceptions show via Timber
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Timber.e(throwable, "Uncaught exception on thread %s - app will close", thread.name)
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }
}
