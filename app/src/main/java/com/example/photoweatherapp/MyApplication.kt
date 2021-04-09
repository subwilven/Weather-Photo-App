package com.example.photoweatherapp

import android.app.Application
import com.example.photoweatherapp.di.dataModules
import com.example.photoweatherapp.di.viewModelModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(viewModelModules, dataModules)
        }
    }

}