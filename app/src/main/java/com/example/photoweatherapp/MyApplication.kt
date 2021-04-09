package com.example.photoweatherapp

import android.annotation.SuppressLint
import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.photoweatherapp.di.dataModules
import com.example.photoweatherapp.di.networkModules
import com.example.photoweatherapp.di.viewModelModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {


    companion object {
        @SuppressLint("StaticFieldLeak")
        @get:Synchronized
        var instance: MyApplication? = null
            private set
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin {
            androidContext(this@MyApplication)
            modules(viewModelModules, dataModules,networkModules)
        }
    }

}