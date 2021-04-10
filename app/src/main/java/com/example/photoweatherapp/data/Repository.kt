package com.example.photoweatherapp.data

import android.location.Location
import com.example.photoweatherapp.MyApplication
import com.example.photoweatherapp.utils.API_KEY
import com.example.photoweatherapp.utils.FILE_SAVE_DIRECTORY
import java.io.File

class Repository(private val weatherApis: WeatherApis) {

    suspend fun fetchWeatherData(location: Location) =
        weatherApis.fetchWeather(
            location.latitude.toString(),
            location.longitude.toString(),
            "metric",
            API_KEY
        )


    fun getSavedImagesList(): MutableList<File> {
        return MyApplication.instance!!.getExternalFilesDir(FILE_SAVE_DIRECTORY)?.listFiles()
            ?.toMutableList() ?: mutableListOf()
    }
}