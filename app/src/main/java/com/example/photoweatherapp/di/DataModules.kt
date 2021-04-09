package com.example.photoweatherapp.di

import com.example.photoweatherapp.data.Repository
import org.koin.dsl.module

val dataModules = module {
    single { Repository(get()) }
}