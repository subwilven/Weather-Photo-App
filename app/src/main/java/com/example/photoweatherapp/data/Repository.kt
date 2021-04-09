package com.example.photoweatherapp.data

import com.example.photoweatherapp.MyApplication
import com.example.photoweatherapp.utils.API_KEY
import com.example.photoweatherapp.utils.FILE_SAVE_DIRECTORY
import java.io.File

class Repository(private val weatherApis: WeatherApis) {

    suspend fun fetchWeatherData() =
        weatherApis.fetchWeather("29.990895", "31.239367", API_KEY)


    fun getSavedImagesList(): MutableList<File> {
        return File(MyApplication.instance!!.externalCacheDir, FILE_SAVE_DIRECTORY).listFiles()
            ?.toMutableList() ?: mutableListOf()
    }
}