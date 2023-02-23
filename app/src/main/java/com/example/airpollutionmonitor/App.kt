package com.example.airpollutionmonitor

import android.app.Application
import android.util.Log
import timber.log.Timber
import org.koin.core.context.startKoin
import org.koin.android.ext.koin.androidContext
import singleModule
import viewModelModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }
        startKoin{
            androidContext(this@App)
            modules(listOf(singleModule, viewModelModule))
        }
    }


    private class ReleaseTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }
        }
    }
}